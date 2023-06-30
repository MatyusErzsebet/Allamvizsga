using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;
using System;

namespace FurnitureAPI.Data.Models
{
    public class UnconfirmedUserEntity
    {
        [Required]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int Id { get; set; }

        [Required]
        public string Email { get; set; }

        [Required]
        public string FirstName { get; set; }

        [Required]
        public string LastName { get; set; }

        [Required]
        public string Password { get; set; }

        [Required]
        public DateTime? BirthDate { get; set; }

        [Required]
        public string Role { get; set; }

        [Required]
        [StringLength(100, MinimumLength = 100)]
        public string ValidationKey { get; set; }


        [Required]
        public DateTime? ValidationKeyExpirationDate { get; set; }
    }
}
