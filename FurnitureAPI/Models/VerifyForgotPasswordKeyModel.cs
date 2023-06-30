using System.ComponentModel.DataAnnotations;

namespace FurnitureAPI.Models
{
    public class VerifyForgotPasswordKeyModel
    {
        [Required(ErrorMessage = "Email is required")]
        [EmailAddress(ErrorMessage = "Not an email address")]
        public string Email { get; set; }

        [Required(ErrorMessage = "Email is required")]
        [StringLength(8, MinimumLength = 8, ErrorMessage = "Key length must be 8 characters")]
        public string Key { get; set; }
    }
}
