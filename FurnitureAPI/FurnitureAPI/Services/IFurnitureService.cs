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
        public Task<FurnitureServiceResponses> UpdateFurnitre(UpdateFurnitureModel model, FurnitureEntity furniture);
        public Task<FurnitureWithReviewsEntity> GetFurnitureWithReviewsById(int id);
        public Task<FurnitureTypeEntity> GetFurnitureTypeById(int id);
    }
}
