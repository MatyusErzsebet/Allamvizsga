package com.example.furnitureapp.datalayers.repositories

import android.util.Log
import com.example.furnitureapp.datalayers.retrofit.RetrofitInstance
import com.example.furnitureapp.datalayers.retrofit.models.*
import com.example.furnitureapp.models.Furniture
import com.example.furnitureapp.models.FurnitureType
import com.example.furnitureapp.utils.ErrorMessageHandler

class FurnitureRepository {


    @Throws(Exception::class)
    suspend fun getFurnitures(token: String): List<FurnitureModel> {

        var getFurnituresResult: List<FurnitureModel>? = null
        var errorMessage: String? = null
        val response = RetrofitInstance.getFurnitures(token)
        if (response == null) {
            errorMessage = "Error getting furnitures"
        } else {
            if (response.isSuccessful) {
                Log.d("furnituresResponse", response.body()?.result.toString())
                getFurnituresResult = response.body()!!.result
            }
            else {
                errorMessage = ErrorMessageHandler.GetErrorMessage(response.errorBody()!!)
            }
        }

        if (errorMessage != null) {
            throw Exception(errorMessage)
        }

        return getFurnituresResult!!

    }

    suspend fun getFurnitureById(token: String, id: Int): FurnitureWithReviewsModel? {
        var getFurnitureByIdResult: FurnitureWithReviewsModel? = null
        var errorMessage: String? = null
        val response = RetrofitInstance.getFurnitureById(token, id)
        if (response == null) {
            errorMessage = "Error getting furniture by id"
        } else {
            if (response.isSuccessful) {
                Log.d("furnituresResponse", response.body()?.result.toString())
                getFurnitureByIdResult = response.body()!!.result
            }
            else {
                errorMessage = ErrorMessageHandler.GetErrorMessage(response.errorBody()!!)
            }
        }

        if (errorMessage != null) {
            throw Exception(errorMessage)
        }

        return getFurnitureByIdResult!!

    }

    suspend fun getFurnitureTypes(token: String): List<FurnitureTypeModel>? {
        var getFurnitureTypesResult: List<FurnitureTypeModel>? = null
        var errorMessage: String? = null
        val response = RetrofitInstance.getFurnitureTypes(token)
        Log.d("furnitureTypesResponse", response?.body()?.result.toString())
        if (response == null) {
            errorMessage = "Error getting furniture types"
        } else {
            if (response.isSuccessful) {

                getFurnitureTypesResult = response.body()!!.result
            }
            else {
                errorMessage = ErrorMessageHandler.GetErrorMessage(response.errorBody()!!)
            }
        }

        if (errorMessage != null) {
            throw Exception(errorMessage)
        }

        return getFurnitureTypesResult

    }

    suspend fun addFurniture(token: String, addFurnitureModel: AddFurnitureModel): AddFurnitureResultModel? {
        var addFurnitureResult: AddFurnitureResultModel? = null
        var errorMessage: String? = null
        val response = RetrofitInstance.addFurniture(token, addFurnitureModel)
        if (response == null) {
            errorMessage = "Error adding furniture"
        } else {
            if (response.isSuccessful) {

                addFurnitureResult = response.body()!!.result
            }
            else {
                errorMessage = ErrorMessageHandler.GetErrorMessage(response.errorBody()!!)
            }
        }

        if (errorMessage != null) {
            throw Exception(errorMessage)
        }

        return addFurnitureResult

    }

    suspend fun updateFurniture(token: String, updateFurnitureModel: UpdateFurnitureModel): Furniture? {
        var updateFurnitureResult: Furniture? = null
        var errorMessage: String? = null
        val response = RetrofitInstance.updateFurniture(token, updateFurnitureModel)

        if (response == null) {
            errorMessage = "Error getting furnitures"
        } else {
            if (response.isSuccessful) {

                updateFurnitureResult = response.body()!!.result
                Log.d("update", updateFurnitureResult.toString())
            }
            else {
                errorMessage = ErrorMessageHandler.GetErrorMessage(response.errorBody()!!)
            }
        }

        if (errorMessage != null) {
            throw Exception(errorMessage)
        }

        return updateFurnitureResult

    }


}