package com.example.furnitureapp.datalayers.retrofit

import com.example.furnitureapp.datalayers.retrofit.models.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://6df8-92-84-24-20.ngrok-free.app/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient())
            .build()
    }

    private val service: RetrofitInterface by lazy {
        retrofit.create(RetrofitInterface::class.java)
    }

    suspend fun login(email: String, password:String) = service.login(LoginCredentials(email, password))
    suspend fun getFurnitures(token: String) = service.getFurnitures(token)
    suspend fun registration(email: String, firstName: String, lastName: String, password:String, birthDate: String) = service.registration(
        RegistrationModel(email, firstName, lastName, password, birthDate)
    )
    suspend fun sendForgotPasswordKey(email: String) = service.sendForgotPasswordKey(
        SendForgotPasswordModel(email)
    )
    suspend fun  verifyForgotPasswordKey(email: String, key: String) = service.verifyForgotPasswordKey(
        VerifyForgotPasswordKeyModel(email,key)
    )
    suspend fun changePassword(email: String, password: String) = service.changePassword(LoginCredentials(email, password))

    suspend fun getFurnitureById(token: String, id: Int) = service.getFurntureById(token, id)
    suspend fun addReview(token: String, addReviewModel: AddReviewModel) = service.addReview(token, addReviewModel)
    suspend fun addPurchase(token: String, addPurchaseModel: AddPurchaseModel) = service.addPurchase(token, addPurchaseModel)
    suspend fun getUsers(token: String) = service.getUsers(token)
    suspend fun getAllPurchases(token: String) = service.getAllPurchases(token)
    suspend fun getFurnitureTypes(token: String) = service.getFurnitureTypes(token)
    suspend fun addFurniture(token: String, addFurnitureModel: AddFurnitureModel) = service.addFurniture(token, addFurnitureModel)
    suspend fun updateFurniture(token: String, updateFurnitureModel: UpdateFurnitureModel) = service.updateFurniture(token, updateFurnitureModel)
}
