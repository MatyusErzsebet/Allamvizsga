using AutoMapper;
using FurnitureAPI.Data.Models;
using FurnitureAPI.Models;
using FurnitureAPI.Services;
using FurnitureAPI.Services.ServiceResponses;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace FurnitureAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class FurnituresController : ControllerBase
    {
        private readonly IMapper _mapper;
        private readonly IFurnitureService _service;
        public FurnituresController(IMapper mapper, IFurnitureService service) {
            _mapper = mapper;
            _service = service;
        }

        [HttpGet]
        [Authorize(Roles ="User,Admin")]
        public async Task<IActionResult> GetFurnitures()
        {
            try
            {
                return Ok(new BackendResponse<List<Furniture>>(_mapper.Map<List<Furniture>>(await _service.GetFurnitures())));
            }
            catch(Exception ex)
            {
                return StatusCode(500, new BackendResponse<string>(ex.Message));
            }
        }

        [HttpGet]
        [Authorize(Roles = "User,Admin")]
        [Route("{id}")]
        public async Task<IActionResult> GetFurnitureById(int id)
        {
            try
            {
                var furniture = await _service.GetFurnitureWithReviewsById(id);
                if (furniture == null)
                    return NotFound("Furniture with given id not found");
                return Ok(new BackendResponse<FurnitureWithReviews>(_mapper.Map<FurnitureWithReviews>(furniture)));
            }
            catch (Exception ex)
            {
                return StatusCode(500, ex.Message);
            }
        }

        [HttpPost]
        [Authorize(Roles = "Admin")]
        public async Task<IActionResult> AddFurniture(AddFurnitureModel model)
        {
            try
            {
                var furniture = _mapper.Map<FurnitureEntity>(model);
                var result = await _service.AddFurniture(furniture);
                switch (result)
                {
                    case FurnitureServiceResponses.NOTFOUND:
                    return NotFound(new BackendResponse<string>("Furniture type with given id not found"));

                    case FurnitureServiceResponses.SUCCESS:
                    var furnitureModel = _mapper.Map<AddFurnitureResultModel>(furniture);
                    furnitureModel.FurnitureTypeName = (await _service.GetFurnitureTypeById(furnitureModel.FurnitureTypeId)).Name;
                    
                    return StatusCode(201, new BackendResponse<AddFurnitureResultModel>(furnitureModel));

                    default:
                    return StatusCode(500, new BackendResponse<string>("Error adding furniture"));
                }
                
            }
            catch (Exception ex)
            {
                return StatusCode(500, new BackendResponse<string>(ex.Message));
            }
        }

        [HttpPut]
        [Authorize(Roles = "Admin")]
        public async Task<IActionResult> UpdateFurniture(UpdateFurnitureModel model)
        {
            try
            {
                var furniture = await _service.GetFurnitureById(model.Id);
                if(furniture == null)
                    return NotFound(new BackendResponse<string>("Furniture with given id not found"));

                var result = await _service.UpdateFurnitre(model, furniture, model.DiscountPercentage);
                switch (result)
                {
                    case FurnitureServiceResponses.NOTFOUND:
                    return NotFound(new BackendResponse<string>("Furniture type with given id not found"));

                    case FurnitureServiceResponses.SUCCESS:
                        var furnitureResultModel = _mapper.Map<Furniture>(furniture);
                        if (model.DiscountPercentage == null)
                            furnitureResultModel.DiscountPercentage = 0;
                        else
                            furnitureResultModel.DiscountPercentage = model.DiscountPercentage.Value;

                    return StatusCode(200, new BackendResponse<Furniture>(furnitureResultModel));

                    default:
                        return StatusCode(500, new BackendResponse<string>("Error adding furniture"));
                }

            }
            catch (Exception ex)
            {
                return StatusCode(500, new BackendResponse<string>(ex.Message));
            }
        }

        [HttpGet]
        [Authorize(Roles = "User,Admin")]
        [Route("FurnitureTypes")]
        public async Task<IActionResult> GetFurnitureTypes()
        {
            try
            {
                return Ok(new BackendResponse<List<FurnitureType>>(_mapper.Map<List<FurnitureType>>(await _service.GetFurnitureTypes())));
            }
            catch(Exception ex)
            {
                return StatusCode(500, new BackendResponse<string>(ex.Message));
            }
        }
    }
}
