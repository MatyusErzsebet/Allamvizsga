﻿namespace FurnitureAPI.Data.Models
{
    public class FurnitureWithRatingAverageEntity
    {
        public int Id { get; set; }

        public string Name { get; set; }

        public float Price { get; set; }

        public int FurnitureTypeId { get; set; }
        public string FurnitureTypeName { get; set; }

        public string Description { get; set; }

        public string Size { get; set; }

        public int AvailableQuantity { get; set; }
        public string ImageUrl { get; set; }

        public float RatingAverage { get; set; }
        public int NumberOfReviews { get; set; }
        public int OrdersCount { get; set; }
        public int DiscountPercentage { get; set; }
        public bool IsDeleted { get; set; }
        public string GlbPath { get; set; }
        public float? GlbScale { get; set; }
    }
}
