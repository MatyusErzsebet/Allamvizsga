using System;
using System.ComponentModel.DataAnnotations;

namespace FurnitureAPI.Models
{
    public class AddReviewModel
    {

        [Required(ErrorMessage = "Furniture id is required")]
        public int FurnitureId { get; set; }

        [Required(ErrorMessage = "Rating is required")]
        [Range(1, 5, ErrorMessage = "Rating must be between 1 and 5")]
        public int Rating { get; set; }

        [MaxLength(500, ErrorMessage = "Comment must contain maximum 500 characters")]
        public string Comment { get; set; }


    }
}
