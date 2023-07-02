package com.example.furnitureapp.datalayers.retrofit

import com.example.furnitureapp.datalayers.retrofit.models.*
import com.example.furnitureapp.models.Furniture
import com.example.furnitureapp.models.FurnitureType
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface RetrofitInterface {

    @POST("api/users/login")
    suspend fun login(@Body loginCredentials: LoginCredentials): Response<BackendResponse<LoginResult>>?
    @GET("api/furnitures")
    suspend fun getFurnitures(@Header("Token") token: String): Response<BackendResponse<List<FurnitureModel>>>?
    @POST("api/users/sendregistrationcode")
    suspend fun registration(@Body registrationModel: RegistrationModel): Response<BackendResponse<String>>?
    @POST("api/users/sendforgotpasswordkey")
    suspend fun sendForgotPasswordKey(@Body sendForgotPasswordModel: SendForgotPasswordModel): Response<BackendResponse<String>>?
    @POST("api/users/verifyforgotpasswordkey")
    suspend fun verifyForgotPasswordKey(@Body verifyForgotPasswordKeyModel: VerifyForgotPasswordKeyModel): Response<BackendResponse<String>>?
    @PUT("api/users/changepassword")
    suspend fun changePassword(@Body loginCredentials: LoginCredentials): Response<BackendResponse<String>>?
    @GET("api/furnitures/{id}")
    suspend fun getFurntureById(@Header("Token") token: String, @Path("id") id: Int): Response<BackendResponse<FurnitureWithReviewsModel>>?
    @POST("api/reviews")
    suspend fun addReview(@Header("Token") token: String, @Body addReviewModel: AddReviewModel): Response<BackendResponse<ReviewModel>>?
    @POST("api/purchases")
    suspend fun addPurchase(@Header("Token") token: String, @Body addPurchaseModel: AddPurchaseModel): Response<BackendResponse<String>>?
    @GET("api/users/getallusers")
    suspend fun getUsers(@Header("Token") token: String): Response<BackendResponse<List<UserModel>>>?
    @GET("api/purchases")
    suspend fun getAllPurchases(@Header("Token") token: String): Response<BackendResponse<List<AdminPurchaseModel>>>?
    @GET("api/furnitures/furnituretypes")
    suspend fun getFurnitureTypes(@Header("Token") token: String): Response<BackendResponse<List<FurnitureTypeModel>>>?
    @POST("api/furnitures")
    suspend fun addFurniture(@Header("Token") token: String, @Body addFurnitureModel: AddFurnitureModel): Response<BackendResponse<AddFurnitureResultModel>>?
    @PUT("api/furnitures")
    suspend fun updateFurniture(@Header("Token") token: String, @Body updateFurnitureModel: UpdateFurnitureModel): Response<BackendResponse<Furniture>>?
}