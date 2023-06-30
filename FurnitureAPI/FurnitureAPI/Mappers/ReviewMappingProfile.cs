using AutoMapper;
using FurnitureAPI.Data.Models;
using FurnitureAPI.Models;

namespace FurnitureAPI.AutoMappres
{
    public class ReviewMappingProfile : Profile
    {
        public ReviewMappingProfile()
        {
            CreateMap<ReviewEntity, Review>().ForMember(r => r.UserName, opt => opt.Ignore()).ForMember(r => r.FurnitureName, opt => opt.Ignore());
            CreateMap<ReviewWithUserNamesEntity, Review>();
            CreateMap<AddReviewModel, ReviewEntity>();

        }
    }
}
