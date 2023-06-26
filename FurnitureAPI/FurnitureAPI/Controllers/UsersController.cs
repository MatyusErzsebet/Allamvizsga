using AutoMapper;
using FurnitureAPI.Data.Models;
using FurnitureAPI.Models;
using FurnitureAPI.Services;
using FurnitureAPI.Services.ServiceResponses;
using FurnitureAPI.Utils;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore.Metadata.Internal;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Primitives;
using Microsoft.IdentityModel.Tokens;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.IdentityModel.Tokens.Jwt;
using System.Linq;
using System.Net;
using System.Security.Claims;
using System.Text;
using System.Threading.Tasks;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace FurnitureAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class UsersController : ControllerBase
    {
        private readonly IUserService _userService;
        private readonly IMapper _mapper;
        private readonly IConfiguration _config;

        public UsersController(IUserService userService, IMapper mapper, IConfiguration config)
        {
            _userService = userService;
            _mapper = mapper;
            _config = config;
        }

        [Route("SendRegistrationCode")]
        [HttpPost]
        public async Task<IActionResult> SendRegistrationCode([FromBody] UnconfirmedUser UserModel)
        {
            try
            {
                var user = _mapper.Map<UnconfirmedUserEntity>(UserModel);

                var result = await _userService.SendRegistrationCode(user);

                switch (result)
                {
                    case UserServiceResponses.BADREQUEST: return BadRequest(new BackendResponse<string>("User can't be null"));
                    case UserServiceResponses.ERROR: return StatusCode(500, new BackendResponse<string>("Unconfirmed user could not be added"));
                    case UserServiceResponses.SUCCESS: return StatusCode(201, new BackendResponse<string>("Unconfirmed user added"));
                    case UserServiceResponses.USERALREADYEXISTS: return StatusCode(409, new BackendResponse<string>("User already exists"));
                    case UserServiceResponses.EMAILNOTSENT: return StatusCode(500, new BackendResponse<string>("Email could not be sent."));
                    default : return StatusCode(500, new BackendResponse<string>("Unknown service response"));
                }
            }
            catch(Exception ex)
            {
                return StatusCode(500, new BackendResponse<string>(ex.Message));
            }
        }

        [Route("Registration")]
        [HttpGet]
        public async Task<IActionResult> Registration([FromQuery(Name = "email")] string email, [FromQuery(Name = "code")] string validationKey )
        {
            try
            {
         
                var result = await _userService.RegisterUser(email, validationKey);

                switch (result)
                {
                    case UserServiceResponses.BADREQUEST:
                        return new ContentResult
                        {
                            ContentType = "text/html",
                            StatusCode = (int)HttpStatusCode.BadRequest,
                            Content = "<html><body><div style='font-size: 32px; text-align:center; color:red;'>Validation key mismatch</div></body></html>"
                        };
                    case UserServiceResponses.ERROR:
                        return new ContentResult
                        {
                            ContentType = "text/html",
                            StatusCode = 500,
                            Content = "<html><body><div style='font-size: 32px; text-align:center; color:red;'>Email could not be verified</div></body></html>"
                        };
                    case UserServiceResponses.SUCCESS:
                        return new ContentResult
                        {
                            ContentType = "text/html",
                            StatusCode = 201,
                            Content = "<html><body><div style='font-size: 32px; text-align:center; color:green;'>Email verified</div></body></html>"
                        };
                    case UserServiceResponses.NOTFOUND:
                        return new ContentResult
                        {
                            ContentType = "text/html",
                            StatusCode = 404,
                            Content = "<html><body><div style='font-size: 32px; text-align:center; color:red;'>User with the given email not found or the validation key has expired</div></body></html>"
                        };
                    case UserServiceResponses.EMAIL_NOT_FOUND:
                        return new ContentResult
                        {
                            ContentType = "text/html",
                            StatusCode = (int)HttpStatusCode.BadRequest,
                            Content = "<html><body><div style='font-size: 32px; text-align:center; color:red;'>Email required</div></body></html>"
                        };
                    case UserServiceResponses.VALIDATION_KEY_NOT_FOUND:
                        return new ContentResult
                        {
                            ContentType = "text/html",
                            StatusCode = (int)HttpStatusCode.BadRequest,
                            Content = "<html><body><div style='font-size: 32px; text-align:center; color:red;'>Validation code required</div></body></html>"
                        };
                    default:
                        return new ContentResult
                        {
                            ContentType = "text/html",
                            StatusCode = 500,
                            Content = "<html><body><div style='font-size: 32px; text-align:center; color:red;'>Unknown server error</div></body></html>"
                        };
                }
            }
            catch (Exception ex)
            {
                return StatusCode(500, ex.Message);
            }
        }

        private string GenerateToken(UserEntity User)
        {
            var securityKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_config["Jwt:Key"]));
            var credentials = new SigningCredentials(securityKey, SecurityAlgorithms.HmacSha256);
            var claims = new[]
            {
                new Claim(ClaimTypes.Name,User.Email),
                new Claim(ClaimTypes.Role,User.Role)
            };
            var token = new JwtSecurityToken(_config["Jwt:Issuer"],
                _config["Jwt:Audience"],
                claims,
                expires: DateTime.Now.AddMinutes(1000),
                signingCredentials: credentials);


            return new JwtSecurityTokenHandler().WriteToken(token);

        }

        [HttpPost]
        [Route("Login")]
        public async Task<IActionResult> Login(LoginModel loginModel)
        {
            try
            {
                var result = await _userService.Login(loginModel.Email, loginModel.Password);
                switch (result)
                {
                    case UserServiceResponses.BADREQUEST: return BadRequest(new BackendResponse<string>("Password mismatch"));
                    case UserServiceResponses.NOTFOUND: return NotFound(new BackendResponse<string>("User with given email not found"));
                    case UserServiceResponses.SUCCESS:
                        var user = await _userService.GetUserByEmail(loginModel.Email);
                        var userModel = _mapper.Map<UserModel>(user);
                        return StatusCode(201, new BackendResponse<UserTokenModel>(new UserTokenModel { User = userModel, Token = GenerateToken(user) }));
                    default: return StatusCode(500, new BackendResponse<string>("Unknown service response"));
                }
            }

            catch(Exception ex)
            {
                return StatusCode(500, new BackendResponse<string>(ex.Message));
            }

        }

        [HttpPost]
        [Route("SendForgotPasswordKey")]
        public async Task<IActionResult> SendForgotPasswordKey(SendForgotPasswordKeyModel model)
        {
            try
            { 
                var result = await _userService.SendForgotPasswordKey(model.Email);
                switch (result)
                {
                    case UserServiceResponses.BADREQUEST: return BadRequest(new BackendResponse<string>("Email required"));
                    case UserServiceResponses.NOTFOUND: return NotFound(new BackendResponse<string>("User with given email not found"));
                    case UserServiceResponses.SUCCESS: return StatusCode(201, new BackendResponse<string>("Password change email sent successfully"));
                    case UserServiceResponses.EMAILNOTSENT: return StatusCode(500, new BackendResponse<string>("Email could not be sent."));
                    default: return StatusCode(500, new BackendResponse<string>("Unknown service response"));
                }
            }
            catch (Exception ex)
            {
                return StatusCode(500, new BackendResponse<string>(ex.Message));
            }
        }

        [HttpPost]
        [Route("VerifyForgotPasswordKey")]
        public async Task<IActionResult> VerifyForgotPasswordKey(VerifyForgotPasswordKeyModel model)
        {
            try
            {
                var result = await _userService.VerifyForgotPasswordKey(model.Email, model.Key);
                switch (result)
                {
                    case UserServiceResponses.BADREQUEST: return BadRequest(new BackendResponse<string>("Key mismatch"));
                    case UserServiceResponses.NOTFOUND: return NotFound(new BackendResponse<string>("User with given email not found"));
                    case UserServiceResponses.SUCCESS: return StatusCode(201, new BackendResponse<string>("Password change key verified successfully"));
                    case UserServiceResponses.EMAIL_NOT_FOUND: return StatusCode(500, new BackendResponse<string>("Email is required"));
                    case UserServiceResponses.VALIDATION_KEY_NOT_FOUND: return StatusCode(400, new BackendResponse<string>("Password change key is required"));
                    case UserServiceResponses.FORGOTPASSWORDNOTSETTOUSER: return StatusCode(404, new BackendResponse<string>("Password change was not required by user"));
                    default: return StatusCode(500, new BackendResponse<string>("Unknown service response"));
                }
            }
            catch (Exception ex)
            {
                return StatusCode(500, new BackendResponse<string>(ex.Message));
            }
        }

        [HttpPut]
        [Route("ChangePassword")]
        public async Task<IActionResult> ChangePassword(LoginModel EmailPasswordModel)
        {
            try
            {
                var result = await _userService.UpdatePassword(EmailPasswordModel.Email, EmailPasswordModel.Password);
                switch (result)
                {
                    case UserServiceResponses.BADREQUEST: return BadRequest(new BackendResponse<string>("Email or password is not given"));
                    case UserServiceResponses.NOTFOUND: return NotFound(new BackendResponse<string>("User with given email not found"));
                    case UserServiceResponses.SUCCESS: return StatusCode(201, new BackendResponse<string>("Password updated successfully"));
                    case UserServiceResponses.FORGOTPASSWORDNOTSETTOUSER: return NotFound(new BackendResponse<string>("Password change was not required by user"));
                    case UserServiceResponses.PASSWORDVALIDATIONKEYNOTMATCHEDBEFORE: return BadRequest(new BackendResponse<string>("User did not match the validation key before"));
                    default: return StatusCode(500, new BackendResponse<string>("Unknown service response"));
                }
            }
            catch (Exception ex)
            {
                return StatusCode(500, new BackendResponse<string>(ex.Message));
            }
        }

        [Authorize(Roles = "User,Admin")]
        [HttpGet]
        [Route("GetMyUser")]
        public async Task<IActionResult> GetMyUser()
        {
            try
            {
                StringValues values;
                var res = Request.Headers.TryGetValue("Token", out values);
                var user = await _userService.GetUserFromToken(values);
                if (user == null)
                    return NotFound(new BackendResponse<string>("User with given email not found"));
                return Ok(new BackendResponse<UserModel>(_mapper.Map<UserModel>(user)));
            }

            catch(Exception ex)
            {
                return StatusCode(500, new BackendResponse<string>(ex.Message));
            }
        }

        [Authorize(Roles = "Admin")]
        [HttpGet]
        [Route("GetUserByEmail/{email}")]
        public async Task<IActionResult> GetUserByEmail(string email)
        {
            try
            {
                var result = await _userService.GetUserByEmail(email);
                if (result == null)
                {
                    return NotFound(new BackendResponse<string>("User with given email not found"));
                }

                return Ok(new BackendResponse<UserModel>(_mapper.Map<UserModel>(result)));
            }
            catch (Exception ex)
            {
                return StatusCode(500, new BackendResponse<string>(ex.Message));
            }
        }

        [Authorize(Roles = "Admin")]
        [HttpDelete]
        [Route("DeleteUserByEmail/{email}")]
        public async Task<IActionResult> DeleteUserByEmail(string email)
        {
            try
            {
                var result = await _userService.DeleteUserByEmail(email);

                switch (result)
                {
                    case UserServiceResponses.NOTFOUND: return NotFound(new BackendResponse<string>("User with given email not found"));
                    case UserServiceResponses.SUCCESS: return StatusCode(200, new BackendResponse<string>("User deleted"));
                    default: return StatusCode(500, new BackendResponse<string>("Unknown service response"));
                }
            }

            catch (Exception ex)
            {
                return StatusCode(500, new BackendResponse<string>(ex.Message));
            }

        }

        /*
        [Authorize(Roles = "Admin")]
        [HttpPut]
        [Route("UpdateUser")]
        public async Task<IActionResult> UpdateUser(UpdateUserModel userModel)
        {
            try
            {
                var user = await _userService.GetUserByEmail(userModel.Email);
                if (user == null)
                    return NotFound("User with given email not found");

                var result = await _userService.UpdateUser(user, userModel);

                switch (result)
                {
                    case UserServiceResponses.SUCCESS: return StatusCode(200, _mapper.Map<UserModel>(user));
                    default: return StatusCode(500, "Unknown service response");
                }
            }

            catch (Exception ex)
            {
                return StatusCode(500, ex.Message);
            }

        }

        */

        [Authorize(Roles = "Admin")]
        [HttpGet]
        [Route("GetAllUsers")]
        public async Task<IActionResult> GetAllUsers()
        {
            try
            {
                return Ok(new BackendResponse<List<UserModel>>(_mapper.Map<List<UserModel>>(await _userService.GetAllUsers())));
            }

            catch (Exception ex)
            {
                return StatusCode(500, new BackendResponse<string>(ex.Message));
            }

        }


    }
}
