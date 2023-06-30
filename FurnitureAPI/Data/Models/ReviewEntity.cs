using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace FurnitureAPI.Data.Models
{
    public class ReviewEntity
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
        [Range(1, 5)]
        public int Rating { get; set; }

        [Required]
        public DateTime? Date { get; set; }

        [StringLength(500, MinimumLength = 1)]
        public string Comment { get; set; }

    }
}
