using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace FurnitureAPI.Data.Models
{
    public class PurchaseEntity
    {
        [Required]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int Id { get; set; }

        [Required]
        [ForeignKey(nameof(User))]
        public int UserId { get; set; }
        public UserEntity User { get; set; }

        [Required]
        [ForeignKey(nameof(Furniture))]
        public int FurnitureId { get; set; }
        public FurnitureEntity Furniture { get; set; }

        [Required]
        public DateTime? Date { get; set; }

        [Required]
        [Range(0, float.MaxValue)]
        public float Price { get; set; }

        [Required]
        [Range(1, int.MaxValue)]
        public int Quantity { get; set; }

        [Required]
        [StringLength(300, MinimumLength = 1)]
        public string Address { get; set; }
        
    }
}
