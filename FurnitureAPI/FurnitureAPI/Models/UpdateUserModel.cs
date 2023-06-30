using System;
using System.ComponentModel.DataAnnotations;

namespace FurnitureAPI.Models
{
    public class UpdateUserModel
    {
        [Required(ErrorMessage = "Email required")]
        [EmailAddress(ErrorMessage = "Not an email address")]
        public string Email { get; set; }

        [Required(ErrorMessage = "First name required" )]
        public string FirstName { get; set; }

        [Required(ErrorMessage = "Last name required")]
        public string LastName { get; set; }

        [Required(ErrorMessage = "Birth date required")]
        public DateTime? BirthDate { get; set; }
    }
}
