using FurnitureAPI.Data;
using FurnitureAPI.Data.Models;
using FurnitureAPI.Models;
using FurnitureAPI.Services.ServiceResponses;
using FurnitureAPI.Utils;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Primitives;
using Microsoft.IdentityModel.Tokens;
using Org.BouncyCastle.Asn1.Ocsp;
using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Linq;
using System.Security.Claims;
using System.Text;
using System.Threading.Tasks;
using static System.Net.WebRequestMethods;

namespace FurnitureAPI.Services
{
    public class UserService : IUserService
    {
        private readonly Context _context;

        public UserService(Context context)
        {
            _context = context;
        }

        public async Task<UserServiceResponses> RegisterUser(string email, string validationKey)
        {
            if (email == null)
                return UserServiceResponses.EMAIL_NOT_FOUND;

            if (validationKey == null)
                return UserServiceResponses.VALIDATION_KEY_NOT_FOUND;

            var unconfirmedUser = await _context.UnconfirmedUsers.FirstOrDefaultAsync(uu => string.Equals(uu.Email, email));
            if (unconfirmedUser == null)
                return UserServiceResponses.NOTFOUND;

            if (!string.Equals(unconfirmedUser.ValidationKey, validationKey))
                return UserServiceResponses.BADREQUEST;

           
            _context.Users.Add(new UserEntity { Email = unconfirmedUser.Email, BirthDate = unconfirmedUser.BirthDate,
                FirstName = unconfirmedUser.FirstName, LastName = unconfirmedUser.LastName, Password = unconfirmedUser.Password, Role = "User", InsertedDate = DateTime.Now });

            try
            {
                await _context.SaveChangesAsync();
                return UserServiceResponses.SUCCESS;
            }
            catch(Exception ex)
            {
                return UserServiceResponses.ERROR;
            }
            
        }

        public async Task<UserServiceResponses> SendRegistrationCode(UnconfirmedUserEntity user)
        {
            if (user == null)
                return UserServiceResponses.BADREQUEST;

            var result2 = await _context.Users.FirstOrDefaultAsync(u => string.Equals(u.Email, user.Email));
            if (result2 != null)
                return UserServiceResponses.USERALREADYEXISTS;

            var result = await _context.UnconfirmedUsers.FirstOrDefaultAsync(uu => string.Equals(uu.Email, user.Email));

            if(result != null)
            {
                _context.UnconfirmedUsers.Remove(result);             
            }

            string validationKey;
            UnconfirmedUserEntity SearchedUser;
            do {
                validationKey = ValidationKeyGenerator.GenerateValidationKey();
                SearchedUser = await _context.UnconfirmedUsers.FirstOrDefaultAsync(u => string.Equals(u.ValidationKey, validationKey));
            }
            while (SearchedUser != null);

            user.ValidationKey = validationKey;
            user.ValidationKeyExpirationDate = DateTime.Now.AddMinutes(10);
            user.Role = "User";
            _context.UnconfirmedUsers.Add(user);

            try
            {
                await _context.SaveChangesAsync();
                var link = " https://fdbe-92-84-24-20.ngrok-free.app/api/Users/Registration?email=" + user.Email + "&code=" + user.ValidationKey;
                if(MyEmailSender.SendMail(user.Email, "Registration", "Use this link to verify your user: <a href = " + link + "> link </a>. The verification link is available for 10 minutes.") == false)
                {
                    return UserServiceResponses.EMAILNOTSENT;
                }
                return UserServiceResponses.SUCCESS;
            }
            catch
            {
                return UserServiceResponses.ERROR;
            }
        }

        public async Task<UserServiceResponses> Login(string email, string password)
        {
            var user = await _context.Users.FirstOrDefaultAsync(u => string.Equals(u.Email, email));
            if (user == null)
                return UserServiceResponses.NOTFOUND;

            if (string.Equals(user.Password, password))
                return UserServiceResponses.SUCCESS;

            return UserServiceResponses.BADREQUEST;
        }

        public async Task<UserServiceResponses> SendForgotPasswordKey(string email)
        {
            if (email == null)
                return UserServiceResponses.BADREQUEST;

            var user = await _context.Users.FirstOrDefaultAsync(u => string.Equals(u.Email, email));
            if (user == null)
                return UserServiceResponses.NOTFOUND;

            string changeKey = ValidationKeyGenerator.GenerateForgotPasswordKey();

            var result = _context.UserForgotPasswordKeys.Where(fp => fp.UserId == user.Id).SingleOrDefault();

            if(result != null)
            {
                _context.Entry(result).State = EntityState.Deleted;
                try
                {
                    await _context.SaveChangesAsync();
                }
                catch
                {
                    return UserServiceResponses.ERROR;
                }
            }
            _context.UserForgotPasswordKeys.Add(new UserForgotPasswordKeyEntity { UserId = user.Id, ChangeKey = changeKey, KeyExpirationDate = DateTime.Now.AddMinutes(10), Isverified = false });

            try
            {
                await _context.SaveChangesAsync();
                if(MyEmailSender.SendMail(user.Email, "Password change", "Your forgot password key is " + changeKey + " and is active for 10 minutes.") == false){
                    return UserServiceResponses.EMAILNOTSENT;
                }
                return UserServiceResponses.SUCCESS;
            }
            catch(Exception ex)
            {
                return UserServiceResponses.ERROR;
            }

        }

        public async Task<UserServiceResponses> VerifyForgotPasswordKey(string email, string key)
        {
            if (email == null)
                return UserServiceResponses.EMAIL_NOT_FOUND;

            if (key == null)
                return UserServiceResponses.VALIDATION_KEY_NOT_FOUND;

            var user = await _context.Users.FirstOrDefaultAsync(u => string.Equals(u.Email, email));

            if (user == null)
                return UserServiceResponses.NOTFOUND;

            var userPassword = await _context.UserForgotPasswordKeys.FirstOrDefaultAsync(fp => fp.UserId == user.Id);

            if (userPassword == null)
                return UserServiceResponses.FORGOTPASSWORDNOTSETTOUSER;

            if(!string.Equals(userPassword.ChangeKey.ToLower(), key.ToLower()))
            {
                return UserServiceResponses.BADREQUEST;
            }

            userPassword.Isverified = true;
            _context.Entry(userPassword).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
                return UserServiceResponses.SUCCESS;
            }
            catch
            {
                return UserServiceResponses.ERROR;
            }
        }

        
        public async Task<UserEntity> GetUserByEmail(string email)
        {
            return await _context.Users.FirstOrDefaultAsync(u => string.Equals(u.Email, email));
        }

        public async Task<UserServiceResponses> UpdatePassword(string email, string newPassword)
        {
            if (email == null || newPassword == null)
                return UserServiceResponses.BADREQUEST;

            var user = await GetUserByEmail(email);

            if(user == null)
                return UserServiceResponses.NOTFOUND;

            var userPassword = await _context.UserForgotPasswordKeys.FirstOrDefaultAsync(up => string.Equals(up.UserId, user.Id));

            if(userPassword == null)
            {
                return UserServiceResponses.FORGOTPASSWORDNOTSETTOUSER;
            }
            
            if(userPassword.Isverified == false)
            {
                return UserServiceResponses.PASSWORDVALIDATIONKEYNOTMATCHEDBEFORE;
            }

            user.Password = newPassword;
            _context.Entry(user).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();

                _context.Entry(userPassword).State = EntityState.Deleted;

                try
                {
                    await _context.SaveChangesAsync();

                    return UserServiceResponses.SUCCESS;
                }
                catch
                {
                    return UserServiceResponses.ERROR;
                }
            }
            catch
            {
                return UserServiceResponses.ERROR;
            }
        }

        public async Task<UserServiceResponses> DeleteUserByEmail(string email)
        {
            var user = await GetUserByEmail(email);

            if (user == null)
                return UserServiceResponses.NOTFOUND;

            _context.Entry(user).State = EntityState.Deleted;

            try
            {
                await _context.SaveChangesAsync();
                return UserServiceResponses.SUCCESS;
            }

            catch
            {
                return UserServiceResponses.ERROR;
            }
        }

        public async Task<UserServiceResponses> UpdateUser(UserEntity userToModify, UpdateUserModel userModel)
        {
            

            userToModify.BirthDate = userModel.BirthDate;
            userToModify.FirstName = userModel.FirstName;
            userToModify.LastName = userModel.LastName;
            
            _context.Entry(userToModify).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
                return UserServiceResponses.SUCCESS;
            }
            catch
            {
                return UserServiceResponses.ERROR;
            }

        }

        public async Task<UserEntity> GetUserFromToken(StringValues values)
        {
            var email = TokenClaimHandler.GetJWTTokenClaim(values[0], ClaimTypes.Name);
            return await GetUserByEmail(email);           
        }

        public async Task<UserEntity> GetUserById(int id)
        {
            return await _context.Users.FirstOrDefaultAsync(u => u.Id == id);
        }

        public async Task<List<UserEntity>> GetAllUsers()
        {
            return await _context.Users.ToListAsync();
        }
    }
}