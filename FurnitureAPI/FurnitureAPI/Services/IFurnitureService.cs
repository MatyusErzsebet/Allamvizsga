using FurnitureAPI.Data.Models;
using FurnitureAPI.Models;
using FurnitureAPI.Services.ServiceResponses;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace FurnitureAPI.Services
{
    public interface IFurnitureService
    {
        public Task<List<FurnitureWithRatingAverageEntity>> GetFurnitures();
        public Task<FurnitureEntity> GetFurnitureById(int id);
        public Task<FurnitureServiceResponses> AddFurniture(FurnitureEntity furniture);
        public Task<FurnitureServiceResponses> UpdateFurnitre(UpdateFurnitureModel model, FurnitureEntity furniture, int? discountPercentage);
        public Task<FurnitureWithReviewsEntity> GetFurnitureWithReviewsById(int id);
        public Task<FurnitureTypeEntity> GetFurnitureTypeById(int id);
        public Task<List<FurnitureTypeEntity>> GetFurnitureTypes();
        public Task<FurnitureServiceResponses> AddDiscountToFurniture(int furnitureId, int percentage);
        public Task<FurnitureServiceResponses> ModifyDiscount(int furnitureId, int? percentage);
    }
}
