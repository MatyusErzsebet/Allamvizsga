using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace FurnitureAPI.Data.Models
{
    public class DiscountEntity
    {
        [Required]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int Id { get; set; }

        [Required]
        [ForeignKey(nameof(Furniture))]
        public int FurnitureId { get; set;}
        public FurnitureEntity Furniture { get; set;}

        [Required]
        [Range(1, 100)]
        public int Percentage { get; set; }

        [Required]
        public DateTime? DeadLine { get; set; }
    }
}
