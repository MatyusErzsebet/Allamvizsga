using AutoMapper;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using FurnitureAPI.Services;
using FurnitureAPI.Models;
using FurnitureAPI.Data.Models;
using Microsoft.Extensions.Primitives;
using FurnitureAPI.Services.ServiceResponses;

namespace furnitureApp.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ReviewsController: ControllerBase
    {
        private readonly IReviewService _reviewService;
        private readonly IUserService _userService;
        private IMapper _mapper;

        public ReviewsController(IReviewService reviewService, IUserService userService, IMapper mapper)
        {
            _reviewService = reviewService;
            _userService = userService;
            _mapper = mapper;
        }

        [HttpPost]
        [Authorize(Roles = "User")]
        public async Task<IActionResult> AddReview(AddReviewModel review)
        {
            try
            {
                var reviewEntity = _mapper.Map<ReviewEntity>(review);

                StringValues values;
                var res = Request.Headers.TryGetValue("Token", out values);
                var user = await _userService.GetUserFromToken(values);

                var result = await _reviewService.AddReview(reviewEntity, user.Id);

                if(result == ReviewServiceResponses.NULLPARAM)
                    return BadRequest("Review to add can't be null");
        

                if(user == null)
                    return NotFound("User with given id not found");

                if (!string.Equals(user.Role,"User"))
                    return NotFound("User with given id is not a simple user");

                var furniture = await _reviewService.GetFurnitureById(review.FurnitureId);

                if (furniture == null)
                    return NotFound("furniture with given id not found");
            

                if (result == ReviewServiceResponses.EXCEPTION)
                    return StatusCode(500, "Review could't be added");

                var reviewResult = _mapper.Map<Review>(reviewEntity);
                reviewResult.UserName = user.FirstName + " " + user.LastName;
                reviewResult.FurnitureName = furniture.Name;

                return StatusCode(201, new BackendResponse<Review>(reviewResult));
            
            }
            catch(Exception ex)
            {
                return StatusCode(500, ex.Message);
            }
        }

        /*
        [HttpGet]
        public async Task<IActionResult> GetReviews()
        {
            var reviews = await _reviewService.GetReviews();

            try
            {
                return StatusCode(200, _mapper.Map<List<Review>>(reviews));
            }
            catch(Exception ex)
            {
                return StatusCode(500, ex.Message);
            }
        }

        */

        [HttpGet]
        [Authorize(Roles = "User,Admin")]
        [Route("FurnitureReviews/{furnitureId}")]
        public async Task<IActionResult> GetFurnitureReviews(int furnitureId)
        {
            var furniture = await _reviewService.GetFurnitureById(furnitureId);

            if(furniture == null)
                return NotFound("furniture with given id not found");

            var reviews = await _reviewService.GetFurnitureReviews(furnitureId);
             
            try
            {
                return StatusCode(200, _mapper.Map<List<Review>>(reviews));
            }
            catch (Exception ex)
            {
                return StatusCode(500, ex.Message);
            }
        }

        /*
        [HttpGet]
        [Route("{id}")]
        public async Task<IActionResult> GetReviewById(int id)
        {
            var review = await _reviewService.GetReviewById(id);

            if (review == null)
                return NotFound("Review with given id not found");

            try
            {
                return StatusCode(200, _mapper.Map<Review>(review));
            }
            catch (Exception ex)
            {
                return StatusCode(500, ex.Message);
            }
        }

        */

        [HttpDelete]
        [Authorize(Roles = "User")]
        [Route("{id}")]
        public async Task<IActionResult> DeleteReviewById(int id)
        {
            StringValues values;
            var res = Request.Headers.TryGetValue("Token", out values);
            var user = await _userService.GetUserFromToken(values);

            var result = await _reviewService.DeleteReviewById(id, user.Id);

            if (result == ReviewServiceResponses.REVIEWNOTFOUND)
                return NotFound("Review with given id not found");

            if (result == ReviewServiceResponses.EXCEPTION)
                return StatusCode(500, "Review could't be deleted");

            if (result == ReviewServiceResponses.BADREQUEST)
                return StatusCode(500, "Review doesn't belong to user");

            try
            {
                return StatusCode(200, string.Format("Review with id {0} deleted", id));
            }
            catch (Exception ex)
            {
                return StatusCode(500, ex.Message);
            }
        }


    }
}
