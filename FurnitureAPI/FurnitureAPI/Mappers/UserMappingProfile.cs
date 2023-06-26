using AutoMapper;
using FurnitureAPI.Data.Models;
using FurnitureAPI.Models;

namespace FurnitureAPI.Mappers
{
    public class UserMappingProfile: Profile
    {
        public UserMappingProfile()
        {
            CreateMap<UnconfirmedUser, UnconfirmedUserEntity>();
            CreateMap<UserEntity, UserModel>();
        }
    }
}
