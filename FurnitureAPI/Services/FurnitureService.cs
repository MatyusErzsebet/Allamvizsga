using FurnitureAPI.Data;
using FurnitureAPI.Data.Models;
using FurnitureAPI.Models;
using FurnitureAPI.Services.ServiceResponses;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace FurnitureAPI.Services
{
    public class FurnitureService : IFurnitureService
    {
        private readonly Context _context;
        public FurnitureService(Context context) {
            _context = context;
        }

        public async Task<FurnitureTypeEntity> GetFurnitureTypeById(int id)
        {
            return await _context.FurnitureTypes.FirstOrDefaultAsync(ft => ft.Id == id);
        }

        public async Task<FurnitureServiceResponses> AddFurniture(FurnitureEntity furniture)
        {
            var furntureType = await GetFurnitureTypeById(furniture.FurnitureTypeId);

            if (furntureType == null)
                return FurnitureServiceResponses.NOTFOUND;

            
            _context.Furnitures.Add(furniture);

            try
            {
                await _context.SaveChangesAsync();
                return FurnitureServiceResponses.SUCCESS;  
            }
            catch
            {
                return FurnitureServiceResponses.ERROR;
            }
        }


        public async Task<List<FurnitureWithRatingAverageEntity>> GetFurnitures()
        {
            var list = await _context.Furnitures.Where(f => f.IsDeleted == false).ToListAsync();
            List<FurnitureWithRatingAverageEntity> result = new();

            foreach(var f in list)
            {
                var furnitureType = await GetFurnitureTypeById(f.FurnitureTypeId);
                var reviewsForFurnitureCt = _context.Reviews.Where(r => r.FurnitureId == f.Id).Count();
                float ratingAverage;
                if (reviewsForFurnitureCt == 0)
                    ratingAverage = 0;
                else
                    ratingAverage = (float)_context.Reviews.Where(r => r.FurnitureId == f.Id).Average(r => r.Rating);

                var ordersCounter = _context.Purchases.Where(p => p.FurnitureId == f.Id).Sum(p => p.Quantity);

                var discountPercentage = (await _context.Discounts.FirstOrDefaultAsync(d => d.FurnitureId == f.Id))?.Percentage;
                if (discountPercentage == null)
                    discountPercentage = 0;
               
                result.Add(new FurnitureWithRatingAverageEntity {IsDeleted = f.IsDeleted, DiscountPercentage = discountPercentage.Value, OrdersCount = ordersCounter, NumberOfReviews = reviewsForFurnitureCt, AvailableQuantity = f.AvailableQuantity, Description = f.Description, FurnitureTypeId = f.FurnitureTypeId, Id = f.Id, ImageUrl = f.ImageUrl, Name = f.Name, Price = f.Price, Size = f.Size, FurnitureTypeName = furnitureType.Name, RatingAverage = ratingAverage });
            }

            return result;
        }

        public async Task<FurnitureEntity> GetFurnitureById(int id)
        {
            return await _context.Furnitures.FirstOrDefaultAsync(f => f.Id == id);
        }

        public async Task<FurnitureServiceResponses> UpdateFurnitre(UpdateFurnitureModel model, FurnitureEntity furniture)
        {
            furniture.AvailableQuantity = model.AvailableQuantity;
            furniture.IsDeleted = model.IsDeleted;
            furniture.FurnitureTypeId = model.FurnitureTypeId;
            furniture.Price = model.Price;
            furniture.Description = model.Description;
            furniture.Name = model.Name;
            furniture.Size = model.Size;

            _context.Entry(furniture).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
                return FurnitureServiceResponses.SUCCESS;
            }
            catch
            {
                return FurnitureServiceResponses.ERROR;
            }

        }

        public async Task<FurnitureWithReviewsEntity> GetFurnitureWithReviewsById(int id)
        {
            var furniture = await GetFurnitureById(id);
            if (furniture == null)
                return null;

            var furnitureType = await GetFurnitureTypeById(furniture.FurnitureTypeId);
   

            var ordersCounter = _context.Purchases.Where(p => p.FurnitureId == id).Sum(p => p.Quantity);

            var discountPercentage = (await _context.Discounts.FirstOrDefaultAsync(d => d.FurnitureId == id))?.Percentage;
            if (discountPercentage == null)
                discountPercentage = 0;
            List<ReviewWithUserNamesEntity> reviews = new();
            foreach(var r in await _context.Reviews.Where(r => r.FurnitureId == id).ToListAsync())
            {
                var user = await _context.Users.FirstOrDefaultAsync(u => u.Id == r.UserId);
                reviews.Add(new ReviewWithUserNamesEntity {Date = r.Date.Value, Comment = r.Comment, FurnitureId = r.FurnitureId, FurnitureName = furniture.Name, Id = r.Id, Rating = r.Rating, UserId = r.UserId, UserName = user.FirstName + " " + user.LastName });
            }

            float ratingAverage;
            if (reviews.Count == 0)
                ratingAverage = 0;
            else
                ratingAverage = (float)reviews.Average(r => r.Rating);

            var list = new List<KeyValuePair<int, float>>();
            foreach (var f in (await _context.Furnitures.Where(f => f.IsDeleted == false).ToListAsync()))
            {
                var reviewsForFurniture = _context.Reviews.Where(r => r.FurnitureId == f.Id);
                var reviewsCtForFurniture = reviewsForFurniture.Count();
                if (reviewsCtForFurniture == 0)
                {
                    list.Add(new KeyValuePair<int, float>(f.Id, 0.0f));
                }
                else
                {
                    list.Add(new KeyValuePair<int, float>(f.Id, (float)_context.Reviews.Where(r => r.FurnitureId == f.Id).Average(r => r.Rating)));
                }
            }

            list = list.OrderByDescending(kv => kv.Value).ToList();
            int rewPosition = 0;
            for(int i = 0; i < list.Count(); i++)
            {
                if (list[i].Key == id)
                {
                    rewPosition = i+1;
                    break;
                }
            }

            var list2 = new List<KeyValuePair<int, int>>();
            foreach (var f in await _context.Furnitures.Where(f => f.IsDeleted == false).ToListAsync())
            {
                var purchasesForFurniture = _context.Purchases.Where(r => r.FurnitureId == f.Id);
                var purchasesCtForFurniture = purchasesForFurniture.Count();
                if (purchasesCtForFurniture == 0)
                {
                    list2.Add(new KeyValuePair<int, int>(f.Id, 0));
                }
                else
                {
                    list2.Add(new KeyValuePair<int, int>(f.Id, _context.Purchases.Where(p => p.FurnitureId == f.Id).Sum(p => p.Quantity)));
                }
            }

            list2 = list2.OrderByDescending(kv => kv.Value).ToList();
            int purcPosition = 0;
            for (int i = 0; i < list2.Count(); i++)
            {
                if (list2[i].Key == id)
                {
                    purcPosition = i+1;
                    break;
                }
            }

            return new FurnitureWithReviewsEntity { OrdersPosition = purcPosition, ReviewsPosition = rewPosition, RatingAverage = ratingAverage, NumberOfReviews = reviews.Count, DiscountPercentage = discountPercentage.Value, OrdersCount = ordersCounter, AvailableQuantity = furniture.AvailableQuantity, Description = furniture.Description, Name = furniture.Name, FurnitureTypeId = furniture.FurnitureTypeId, Id = furniture.Id, ImageUrl = furniture.ImageUrl, IsDeleted = furniture.IsDeleted, Price = furniture.Price, Size = furniture.Size, FurnitureTypeName = furnitureType.Name, Reviews = reviews};
        }
    }
}
