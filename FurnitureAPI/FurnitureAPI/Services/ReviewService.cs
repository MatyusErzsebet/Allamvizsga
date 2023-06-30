
using FurnitureAPI.Data;
using FurnitureAPI.Data.Models;
using FurnitureAPI.Services;
using FurnitureAPI.Services.ServiceResponses;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace FurnitureAPI.DataAccesLayer
{
    public class ReviewService : IReviewService
    {
        private readonly Context _context;

        public ReviewService(Context context)
        {
            _context = context;
        }
        public async Task<ReviewServiceResponses> AddReview(ReviewEntity review, int userId)
        {
            if (review == null)
                return ReviewServiceResponses.NULLPARAM;

            review.UserId = userId;
            review.Date = DateTime.Now;

            _context.Reviews.Add(review);

            try
            {
                await _context.SaveChangesAsync();
                return ReviewServiceResponses.SUCCESS;
            }
            catch(Exception ex)
            {
                return ReviewServiceResponses.EXCEPTION;
            }
        }

        public async Task<ReviewWithUserNamesEntity> GetReviewById(int id)
        {
            var list = _context.Reviews as IQueryable<ReviewEntity>;

            return await list.Where(r => r.Id == id).Join(_context.Users, r => r.UserId, u => u.Id, (r, u) => new { review = r, user = u })
                .Join(_context.Furnitures, r2 => r2.review.FurnitureId, f2 => f2.Id, (r2, f2) => new ReviewWithUserNamesEntity { Id = r2.review.Id, UserId=r2.user.Id, UserName = r2.user.FirstName + ' ' + r2.user.LastName, FurnitureName = f2.Name, Comment = r2.review.Comment, Rating = r2.review.Rating, FurnitureId = f2.Id }).FirstOrDefaultAsync();

        }

        public async Task<List<ReviewWithUserNamesEntity>> GetReviews()
        {
            var list = _context.Reviews as IQueryable<ReviewEntity>;

            return await list.Join(_context.Users, r => r.UserId, u => u.Id, (r, u) => new { review = r, user = u })
                .Join(_context.Furnitures, r2 => r2.review.FurnitureId, f2 => f2.Id, (r2, f2) => new ReviewWithUserNamesEntity { Id = r2.review.Id, UserId = r2.user.Id,UserName = r2.user.FirstName + ' ' + r2.user.LastName, FurnitureName = f2.Name, Comment = r2.review.Comment, Rating = r2.review.Rating, FurnitureId = f2.Id }).ToListAsync();

        }

        public async Task<List<ReviewWithUserNamesEntity>> GetFurnitureReviews(int furnitureId)
        {
            var list = _context.Reviews as IQueryable<ReviewEntity>;

            return await list.Where(r => r.FurnitureId == furnitureId).Join(_context.Users, r => r.UserId, u => u.Id, (r, u) => new { review = r, user = u })
                .Join(_context.Furnitures, r2 => r2.review.FurnitureId, f2 => f2.Id, (r2, f2) => new ReviewWithUserNamesEntity { Id = r2.review.Id, UserId = r2.user.Id, UserName = r2.user.FirstName + ' ' + r2.user.LastName, FurnitureName = f2.Name, Comment = r2.review.Comment, Rating = r2.review.Rating, FurnitureId = f2.Id }).ToListAsync();

        }

        public async Task<UserEntity> GetUserById(int id)
        {
            return await _context.Users.FirstOrDefaultAsync(u => u.Id == id);
        }

        public async Task<FurnitureEntity> GetFurnitureById(int id)
        {
            return await _context.Furnitures.FirstOrDefaultAsync(p => p.Id == id);
        }

        public async Task<ReviewServiceResponses> DeleteReviewById(int id, int userId)
        {
            var result = await _context.Reviews.FirstOrDefaultAsync(r => r.Id == id);
            if (result == null)
                return ReviewServiceResponses.REVIEWNOTFOUND;

            if (result.UserId != userId)
                return ReviewServiceResponses.BADREQUEST;

            _context.Remove(result);
            try
            {
                await _context.SaveChangesAsync();
                return ReviewServiceResponses.SUCCESS;
            }
            catch
            {
                return ReviewServiceResponses.EXCEPTION;
            }
        }

    }
}
