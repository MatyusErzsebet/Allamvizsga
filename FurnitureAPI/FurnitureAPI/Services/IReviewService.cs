using FurnitureAPI.Data.Models;
using FurnitureAPI.Services.ServiceResponses;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace FurnitureAPI.Services
{
    public interface IReviewService
    {
        public Task<ReviewServiceResponses> AddReview(ReviewEntity review, int userId);
        public Task<List<ReviewWithUserNamesEntity>> GetReviews();
        public Task<UserEntity> GetUserById(int id);
        public Task<ReviewWithUserNamesEntity> GetReviewById(int id);
        public Task<FurnitureEntity> GetFurnitureById(int id);
        public Task<List<ReviewWithUserNamesEntity>> GetFurnitureReviews(int furnitureId);
        public Task<ReviewServiceResponses> DeleteReviewById(int id, int userId);
    }
}
