using FurnitureAPI.Data.Models;
using System.Collections.Generic;
using System;

namespace FurnitureAPI.Models
{
    public class AdminPurchase
    {
        public string UserName { get; set; }
        public DateTime OrderDate { get; set; }
        public float Price { get; set; }
        public string Address { get; set; }
        public List<FurnitureWithTypeNameAndQuantity> Furnitures { get; set; }
    }
}
