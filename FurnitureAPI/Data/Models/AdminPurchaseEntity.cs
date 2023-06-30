using System;
using System.Collections.Generic;

namespace FurnitureAPI.Data.Models
{
    public class AdminPurchaseEntity
    {
        public string UserName { get; set; }
        public DateTime OrderDate { get; set; }
        public float Price { get; set; }
        public string Address { get; set; }
        public List<FurnitureWithTypeNameAndQuantityEntity> Furnitures { get; set;}

    }
}
