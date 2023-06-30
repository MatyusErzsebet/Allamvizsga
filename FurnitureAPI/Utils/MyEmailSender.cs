using MimeKit;
using MailKit.Net.Smtp;
using System.Net.Mail;
using System.Net;
using System;

namespace FurnitureAPI.Utils
{
    public static class MyEmailSender
    {
        public static bool SendMail(string email, string subject, string text)
        {
            var fromAddress = new MailAddress("matyusb.erzsebet@gmail.com", "Betty Matyus");
            var toAddress = new MailAddress(email);
            const string fromPassword = "uzoiybnxmvutdmhv";


            var smtp = new System.Net.Mail.SmtpClient
            {
                Host = "smtp.gmail.com",
                Port = 587,
                EnableSsl = true,
                DeliveryMethod = SmtpDeliveryMethod.Network,
                UseDefaultCredentials = false,
                Credentials = new NetworkCredential(fromAddress.Address, fromPassword)
            };
            var message = new MailMessage(fromAddress, toAddress)
            {
                Subject = subject,
                Body = text
            };

            message.IsBodyHtml = true;
                           
            try
            {
                smtp.Send(message);
                return true;
            }
            catch(Exception ex)
            {
                return false;
            }
            
        }
    }
}

