namespace FurnitureAPI.Data.Models
{
    public class FurnitureWithTypeNameAndQuantityEntity
    {
        public int Id { get; set; }

        public string Name { get; set; }

        public float Price { get; set; }

        public string FurnitureTypeName { get; set; }

        public string Description { get; set; }

        public string Size { get; set; }

        public string ImageUrl { get; set; }

        public int Quantity { get; set; }

        
    }
}
