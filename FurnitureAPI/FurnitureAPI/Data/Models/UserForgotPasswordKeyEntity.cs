using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace FurnitureAPI.Data.Models
{
    public class UserForgotPasswordKeyEntity
    {
        [Required]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int Id { get; set; }

        [Required]
        [ForeignKey(nameof(User))]
        public int UserId { get; set; }
        public UserEntity User { get; set; }

        [Required]
        [StringLength(8, MinimumLength = 8)]
        public string ChangeKey { get; set; }

        [Required]
        public DateTime? KeyExpirationDate { get; set; }

        [Required]
        public bool Isverified { get; set; }
    }
}
