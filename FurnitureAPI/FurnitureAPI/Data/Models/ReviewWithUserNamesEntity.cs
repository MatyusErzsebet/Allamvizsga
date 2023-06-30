using System;

namespace FurnitureAPI.Data.Models
{
    public class ReviewWithUserNamesEntity
    {
        public int Id { get; set; }
        public int UserId { get; set; }
        public string UserName { get; set; }
        public int FurnitureId { get; set; }
        public string FurnitureName { get; set; }
        public int Rating { get; set; }
        public string Comment { get; set; }
        public DateTime Date { get; set; }
    }
}
