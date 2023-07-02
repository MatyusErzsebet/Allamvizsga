package com.example.furnitureapp.datalayers.repositories

import android.util.Log
import com.example.furnitureapp.datalayers.retrofit.RetrofitInstance
import com.example.furnitureapp.datalayers.retrofit.models.AddReviewModel
import com.example.furnitureapp.datalayers.retrofit.models.FurnitureWithReviewsModel
import com.example.furnitureapp.datalayers.retrofit.models.ReviewModel
import com.example.furnitureapp.models.Review
import com.example.furnitureapp.utils.ErrorMessageHandler

class ReviewRepository {
    @Throws(Exception::class)
    suspend fun addReview(token: String, addReviewModel: AddReviewModel): ReviewModel{
        var addReviewResult: ReviewModel? = null
        var errorMessage: String? = null
        val response = RetrofitInstance.addReview(token, addReviewModel)
        if (response == null) {
            errorMessage = "Error getting reviews"
        } else {
            if (response.isSuccessful) {
                Log.d("furnituresResponse", response.body()?.result.toString())
                addReviewResult = response.body()!!.result
            }
            else {
                errorMessage = ErrorMessageHandler.GetErrorMessage(response.errorBody()!!)
            }
        }

        if (errorMessage != null) {
            throw Exception(errorMessage)
        }

        return addReviewResult!!

    }
}