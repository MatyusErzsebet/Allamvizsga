using FurnitureAPI.Data.Models;
using FurnitureAPI.Models;
using FurnitureAPI.Services.ServiceResponses;
using Microsoft.Extensions.Primitives;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace FurnitureAPI.Services
{
    public interface IUserService
    {
        public Task<UserServiceResponses> SendRegistrationCode(UnconfirmedUserEntity user);
        public Task<UserServiceResponses> RegisterUser(string email, string validationKey);
        public Task<UserServiceResponses> Login(string email, string password);
        public Task<UserServiceResponses> SendForgotPasswordKey(string email);
        public Task<UserServiceResponses> VerifyForgotPasswordKey(string email, string key);
        public Task<UserEntity> GetUserByEmail(string email);
        public Task<UserServiceResponses> UpdatePassword(string email, string newPassword);
        public Task<UserServiceResponses> DeleteUserByEmail(string email);
        public Task<UserServiceResponses> UpdateUser(UserEntity userToModify, UpdateUserModel userModel);
        public Task<UserEntity> GetUserFromToken(StringValues values);
        public Task<UserEntity> GetUserById(int id);
        public Task<List<UserEntity>> GetAllUsers();


    }
}
