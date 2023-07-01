using AutoMapper;
using FurnitureAPI.Data.Models;
using FurnitureAPI.Models;
using System.Runtime.ConstrainedExecution;

namespace FurnitureAPI.Mappers
{
    public class FurnitureMappingProfile: Profile
    {
        public FurnitureMappingProfile()
        {
            CreateMap<FurnitureWithTypeNameAndQuantityEntity, FurnitureWithTypeNameAndQuantity>();
            CreateMap<FurnitureEntity, Furniture>();
            CreateMap<AddFurnitureModel, FurnitureEntity>();
            CreateMap<FurnitureWithRatingAverageEntity, Furniture>();
            CreateMap<FurnitureWithReviewsEntity, FurnitureWithReviews>();
            CreateMap<FurnitureEntity, AddFurnitureResultModel>();
            CreateMap<FurnitureTypeEntity, FurnitureType>();
        }
    }
}
