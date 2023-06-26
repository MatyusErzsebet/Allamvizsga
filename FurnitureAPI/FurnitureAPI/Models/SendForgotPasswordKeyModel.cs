using System.ComponentModel.DataAnnotations;

namespace FurnitureAPI.Models
{
    public class SendForgotPasswordKeyModel
    {
        [Required(ErrorMessage = "Email is required")]
        [EmailAddress(ErrorMessage = "Not an email address")]
        public string Email { get; set; }
    }
}
