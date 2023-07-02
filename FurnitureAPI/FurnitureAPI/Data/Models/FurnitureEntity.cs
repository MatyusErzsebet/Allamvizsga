using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace FurnitureAPI.Data.Models
{
    public class FurnitureEntity
    {
        [Required]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int Id { get; set; }

        [Required]
        [StringLength(100, MinimumLength = 1)]
        public string Name { get; set; }

        [Required]
        [Range(1, float.MaxValue)]
        public float Price { get; set; }

        [Required]
        [ForeignKey(nameof(FurnitureType))]
        public int FurnitureTypeId { get; set; }
        public FurnitureTypeEntity FurnitureType { get; set;}

        [Required]
        [StringLength(10000, MinimumLength = 1)]
        public string Description { get; set; }

        [Required]
        [StringLength(30, MinimumLength = 1)]
        public string Size { get; set; }

        [Required]
        public bool IsDeleted { get; set; }

        [Required]
        [Range(0, int.MaxValue)]
        public int AvailableQuantity { get; set; }

        [Required]
        public string ImageUrl { get; set; }

        public string GlbPath { get; set; }
        public float? GlbScale { get; set; }

        public virtual ICollection<ReviewEntity> Reviews { get; set; }
        public virtual ICollection<PurchaseEntity> Purchases { get; set; }


    }
}
