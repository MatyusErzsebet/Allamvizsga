using System.ComponentModel.DataAnnotations;

namespace FurnitureAPI.Models
{
    public class UpdateFurnitureModel
    {
        [Required(ErrorMessage = "Furniture id is required")]
        public int Id { get; set; }

        [Required(ErrorMessage = "Name is required")]
        [StringLength(100, MinimumLength = 1, ErrorMessage = "Name length not between 1 and 100 characters")]
        public string Name { get; set; }

        [Required(ErrorMessage = "Price is required")]
        [Range(1, float.MaxValue, ErrorMessage = "Price can't be less than 1")]
        public float Price { get; set; }

        [Required(ErrorMessage = "Furniture type id is required")]
        public int FurnitureTypeId { get; set; }

        [Required(ErrorMessage = "Description is required")]
        [StringLength(10000, MinimumLength = 1, ErrorMessage = "Description not between 1 and 10000 characters")]
        public string Description { get; set; }

        [Required(ErrorMessage = "Size is required")]
        [StringLength(30, MinimumLength = 1, ErrorMessage = "Size not between 1 and 30 characters")]
        public string Size { get; set; }

        [Required(ErrorMessage = "Available quantity is required")]
        [Range(0, int.MaxValue, ErrorMessage = "Available quantity not greater than 0")]
        public int AvailableQuantity { get; set; }

        [Required(ErrorMessage = "IsDeleted is required")]
        public bool IsDeleted { get; set; }

        [Required(ErrorMessage = "Image url is required")]
        public string ImageUrl { get; set; }
        public int? DiscountPercentage { get; set; }
    }
}
