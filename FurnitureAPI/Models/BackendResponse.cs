namespace FurnitureAPI.Models
{
    public class BackendResponse<T>
    {
        public T Result { get; set; }

        public BackendResponse(T result) {
            Result= result;
        }
    }
}
