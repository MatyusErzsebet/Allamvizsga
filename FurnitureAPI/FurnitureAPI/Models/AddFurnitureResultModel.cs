namespace FurnitureAPI.Models
{
    public class AddFurnitureResultModel
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

    }
}
