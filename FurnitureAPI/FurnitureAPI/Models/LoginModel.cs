using System.ComponentModel.DataAnnotations;

namespace FurnitureAPI.Models
{
    public class LoginModel
    {
        [Required(ErrorMessage = "Email is required")]
        [EmailAddress(ErrorMessage = "Not an email address")]
        public string Email { get; set; }

        [Required(ErrorMessage = "Password is required")]
        [MinLength(8, ErrorMessage = "Password minimum length must be of 8 characters")]
        public string Password { get; set; }
    }
}
