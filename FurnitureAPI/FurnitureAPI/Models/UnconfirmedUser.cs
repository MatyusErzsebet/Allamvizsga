using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;
using System;

namespace FurnitureAPI.Models
{
    public class UnconfirmedUser
    {

        [Required(ErrorMessage = "Please add an email address")]
        [EmailAddress(ErrorMessage = "Not an email")]
        public string Email { get; set; }

        [Required(ErrorMessage = "Please add a first name")]
        public string FirstName { get; set; }

        [Required(ErrorMessage = "Please add a last name")]
        public string LastName { get; set; }

        [Required(ErrorMessage = "Please add a password")]
        [StringLength(50, MinimumLength = 8, ErrorMessage = "Password lenght must be between 8 and 50 characters")]
        public string Password { get; set; }

        [Required(ErrorMessage = "Please add birth date")]
        public DateTime? BirthDate { get; set; }
     

    }
}
