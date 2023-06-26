using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace FurnitureAPI.Services.ServiceResponses
{
    public enum PurchaseServiceResponses
    {
        SUCCESS = 200,
        EXCEPTION = 500,
        FURNITURENOTFOUND = 404,
        NOTENOUGHQUANTITY = 400
    }
}
