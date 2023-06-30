using AutoMapper;
using FurnitureAPI.Data.Models;
using FurnitureAPI.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace FurnitureAPI.Mappers
{
    public class PurchaseMappingProfile : Profile
    {
        public PurchaseMappingProfile()
        {
            CreateMap<Purchase, PurchaseEntity>();
            CreateMap<AdminPurchaseEntity, AdminPurchase>();
            //CreateMap<PictureWithSizePriceQuantityEntity, PictureWithSizePriceQuantity>();
        }
    }
}
