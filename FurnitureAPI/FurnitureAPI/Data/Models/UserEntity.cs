
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace FurnitureAPI.Data.Models
{
    public class UserEntity
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
        public DateTime? InsertedDate { get; set; }


        public virtual ICollection<UserForgotPasswordKeyEntity> UserPasswordChangeKeys{get; set;}
        public virtual ICollection<PurchaseEntity> Purchases { get; set; }
        public virtual ICollection<ReviewEntity> Reviews { get; set; }


    }
}
