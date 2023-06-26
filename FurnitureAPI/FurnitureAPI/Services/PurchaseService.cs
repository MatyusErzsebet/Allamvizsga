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

        /*
        public async Task<List<AdminsPurchase>> GetPurchases()
        {
            var result = new List<AdminsPurchase>();
            var list = _context.Purchases.GroupBy(o => new { o.CustomerId, o.PurchaseDate})
                .Select(g => new { g.Key.CustomerId, g.Key.PurchaseDate});


            foreach (var uo in list)
            {
                var Address = (await _context.Purchases.FirstOrDefaultAsync(o => o.CustomerId == uo.CustomerId && DateTime.Equals(uo.PurchaseDate, o.PurchaseDate))).Address;
                var usersPurchases = new List<PictureWithSizePriceQuantityEntity>();
                var matchingPurchases = _context.Purchases.Where(o => o.CustomerId == uo.CustomerId && DateTime.Equals(uo.PurchaseDate, o.PurchaseDate));
                var list2 = await matchingPurchases.Join(_context.Pictures, o => o.PictureId, p => p.Id, (o, p) => new { a = o, b = p })
                    .Join(_context.PictureContents, p1 => p1.b.ContentTypeId, c => c.Id, (p1, c) => new { a1 = p1, b1 = c})
                    .Join(_context.Sizes, op => op.a1.a.SizeId, s => s.Id, (op, s) => new PictureWithSizePriceQuantityEntity { ImageUrl = op.a1.b.ImageUrl, PictureId = op.a1.b.Id, PictureName = op.a1.b.Name, Quantity = op.a1.a.Quantity, Size = s.Size, Content = op.b1.Name, Price = s.Price}).ToListAsync();

                float sum = 0;

                for(int i=0; i <list2.Count(); i++)
                {
                    var discount = await _context.Discounts.AsNoTracking().FirstOrDefaultAsync(d => d.PictureId == list2[i].PictureId);
                    if (discount != null)
                        list2[i].Price -= list2[i].Price * (float)discount.Percentage / 100;
                    sum += list2[i].Price * list2[i].Quantity;
                }

                var user = await _context.Users.FirstOrDefaultAsync(u => u.Id == uo.CustomerId);
                result.Add(new AdminsPurchase { UserId = user.Id, PurchaseDate = (DateTime)uo.PurchaseDate, Pictures = list2.ToList(), UserName = user.FirstName + " " + user.LastName, Price = sum, Address = Address  });
            }

            return result;
        }
        */
    }
}
