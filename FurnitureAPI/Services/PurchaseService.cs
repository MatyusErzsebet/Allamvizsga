using FurnitureAPI.Data;
using FurnitureAPI.Data.Models;
using FurnitureAPI.Services.ServiceResponses;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace FurnitureAPI.Services
{
    public class PurchaseService : IPurchaseService
    {
        private readonly Context _context;
        public PurchaseService(Context context)
        {
            _context = context;
        }
        public async Task<PurchaseServiceResponses> AddPurchases(List<PurchaseEntity> Purchases, int userId, string Address)
        {
            var PurchaseDate = DateTime.Now;
            foreach (var p in Purchases)
            {
                var furniture = await _context.Furnitures.AsNoTracking().FirstOrDefaultAsync(f => f.Id == p.FurnitureId);
                if ( furniture == null)
                    return PurchaseServiceResponses.FURNITURENOTFOUND;
                if (furniture.AvailableQuantity < p.Quantity)
                    return PurchaseServiceResponses.NOTENOUGHQUANTITY;

                var discount = await _context.Discounts.FirstOrDefaultAsync(d => d.FurnitureId == furniture.Id);

                p.Price = p.Quantity * furniture.Price;
                if (discount != null)
                    p.Price -= (p.Price * discount.Percentage)/100;
                p.UserId = userId;
                p.Date = PurchaseDate;
                p.Address = Address;

                _context.Purchases.Add(p);
                furniture.AvailableQuantity -= p.Quantity;
                _context.Entry(furniture).State = EntityState.Modified;
            }

            try
            {
                await _context.SaveChangesAsync();
                return PurchaseServiceResponses.SUCCESS;
            }
            catch
            {
                return PurchaseServiceResponses.EXCEPTION;
            }


        }

        
        public async Task<List<AdminPurchaseEntity>> GetPurchases()
        {
            var result = new List<AdminPurchaseEntity>();
            var list = _context.Purchases.GroupBy(p => new { p.UserId, p.Date})
                .Select(g => new { g.Key.UserId, g.Key.Date});


            foreach (var up in list)
            {
                var Address = (await _context.Purchases.FirstOrDefaultAsync(p => p.UserId == up.UserId && DateTime.Equals(up.Date, p.Date))).Address;
                var usersPurchases = new List<FurnitureWithTypeNameAndQuantityEntity>();
                var matchingPurchases = _context.Purchases.Where(p => p.UserId == up.UserId && DateTime.Equals(up.Date, p.Date));
                var list2 = await matchingPurchases.Join(_context.Furnitures, p => p.FurnitureId, f => f.Id, (p, f) => new { a = p, b = f })
                    .Join(_context.FurnitureTypes, f1 => f1.b.FurnitureTypeId, c => c.Id, (f1, c) =>  new FurnitureWithTypeNameAndQuantityEntity { ImageUrl = f1.b.ImageUrl, Id = f1.b.Id, Name = f1.b.Name, Size = f1.b.Size, FurnitureTypeName = c.Name, Price = f1.a.Price, Description = f1.b.Description, Quantity = f1.a.Quantity}).ToListAsync();

                float sum = 0;

                for(int i=0; i <list2.Count(); i++)
                {
                    
                    sum += list2[i].Price;
                }

                var user = await _context.Users.FirstOrDefaultAsync(u => u.Id == up.UserId);
                result.Add(new AdminPurchaseEntity { UserName = user.FirstName + " " + user.LastName, OrderDate = up.Date.Value, Furnitures = list2.ToList(), Price = sum, Address = Address  });
            }

            return result;
        }
        
    }
}
