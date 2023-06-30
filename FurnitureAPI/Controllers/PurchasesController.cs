using AutoMapper;
using FurnitureAPI.Data.Models;
using FurnitureAPI.Models;
using FurnitureAPI.Services;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Primitives;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace FurnitureAPI.Controllers { 
    [Route("api/[controller]")]
    [ApiController]
    public class PurchasesController : ControllerBase
    {
        private readonly IPurchaseService _PurchaseService;
        private readonly IUserService _userService;
        private IMapper _mapper;

        public PurchasesController(IPurchaseService PurchaseService, IUserService userService, IMapper mapper)
        {
            _PurchaseService = PurchaseService;
            _userService = userService;
            _mapper = mapper;
        }

        [HttpPost]
        [Authorize(Roles = "User")]
        public async Task<IActionResult> AddPurchase(AddPurchaseModel addPurchaseModel)
        {
            try
            {

                StringValues values;
                var res = Request.Headers.TryGetValue("Token", out values);
                var user = await _userService.GetUserFromToken(values);


                var PurchaseList = _mapper.Map<List<PurchaseEntity>>(addPurchaseModel.Purchases);
                var result = await _PurchaseService.AddPurchases(PurchaseList, user.Id, addPurchaseModel.Address);

                if (result == Services.ServiceResponses.PurchaseServiceResponses.FURNITURENOTFOUND)
                    return NotFound( new BackendResponse<string>("One furniture from the list was not found"));


                if (result == Services.ServiceResponses.PurchaseServiceResponses.NOTENOUGHQUANTITY)
                    return NotFound(new BackendResponse<string>("One furniture from the list has not enough amount in store"));
                if (result == Services.ServiceResponses.PurchaseServiceResponses.EXCEPTION)
                    return StatusCode(500, new BackendResponse<string>("Purchase could not be added"));

                return StatusCode(201, new BackendResponse<string>("Purchase added"));

            }

            catch(Exception ex)
            {
                return StatusCode(500, ex.Message);
            }
        }

        
        [HttpGet]
        //[Authorize(Roles = "Admin")]
        public async Task<IActionResult> GetPurchases()
        {
            try
            {
                var result = _mapper.Map<List<AdminPurchase>>(await _PurchaseService.GetPurchases());
                return StatusCode(200, new BackendResponse<List<AdminPurchase>>( result));
            }
            catch(Exception ex)
            {
                return StatusCode(500, new BackendResponse<string>( ex.Message));
            }
        }
        
    }
}
