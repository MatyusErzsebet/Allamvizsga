package com.example.furnitureapp.datalayers.repositories

import android.util.Log
import com.example.furnitureapp.datalayers.retrofit.RetrofitInstance
import com.example.furnitureapp.datalayers.retrofit.models.AddPurchaseModel
import com.example.furnitureapp.datalayers.retrofit.models.AddReviewModel
import com.example.furnitureapp.datalayers.retrofit.models.AdminPurchaseModel
import com.example.furnitureapp.datalayers.retrofit.models.ReviewModel
import com.example.furnitureapp.utils.ErrorMessageHandler

class ShoppingCartRepository {
    @Throws(Exception::class)
    suspend fun addPurchase(token: String, addPurchaseModel: AddPurchaseModel): String {
        var addPurchaseResult: String? = null
        var errorMessage: String? = null
        val response = RetrofitInstance.addPurchase(token, addPurchaseModel)
        if (response == null) {
            errorMessage = "Error placing order"
        } else {
            if (response.isSuccessful) {
                Log.d("purchaseResponse", response.body()?.result.toString())
                addPurchaseResult = response.body()!!.result
            }
            else {
                errorMessage = ErrorMessageHandler.GetErrorMessage(response.errorBody()!!)
            }
        }

        if (errorMessage != null) {
            throw Exception(errorMessage)
        }

        return addPurchaseResult!!

    }

    suspend fun gettAllPurchases(token: String): List<AdminPurchaseModel> {
        var getAllPurchasesResult: List<AdminPurchaseModel>? = null
        var errorMessage: String? = null
        val response = RetrofitInstance.getAllPurchases(token)
        if (response == null) {
            errorMessage = "Error getting purchases"
        } else {
            if (response.isSuccessful) {
                Log.d("purchaseResponse", response.body()?.result.toString())
                getAllPurchasesResult = response.body()!!.result
            }
            else {
                errorMessage = ErrorMessageHandler.GetErrorMessage(response.errorBody()!!)
            }
        }

        if (errorMessage != null) {
            throw Exception(errorMessage)
        }

        return getAllPurchasesResult!!

    }
}