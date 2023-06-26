using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace FurnitureAPI.Models
{
    public class AddPurchaseModel
    {
        [Required(ErrorMessage = "Purchases is required")]
        public List<Purchase> Purchases { get; set; }

        [Required(ErrorMessage = "Address is required")]
        public string Address { get; set; }
    }
}
