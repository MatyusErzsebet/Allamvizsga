
using FurnitureAPI.Data.Models;
using FurnitureAPI.Services.ServiceResponses;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace FurnitureAPI.Services
{
    public interface IPurchaseService
    {
        public Task<PurchaseServiceResponses> AddPurchases(List<PurchaseEntity> Purchases, int userId, string Address);
        public Task<List<AdminPurchaseEntity>> GetPurchases();
       
    }
}
