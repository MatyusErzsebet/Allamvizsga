package com.example.furnitureapp.viewmodels

import android.app.Application
import android.content.Context
import com.example.furnitureapp.utils.Constants
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.furnitureapp.datalayers.repositories.FurnitureRepository
import com.example.furnitureapp.datalayers.repositories.UserRepository
import com.example.furnitureapp.datalayers.retrofit.models.*
import com.example.furnitureapp.models.Furniture
import com.example.furnitureapp.models.FurnitureType
import com.example.furnitureapp.models.FurnitureWithReviews
import com.example.furnitureapp.models.Review
import kotlinx.coroutines.*

class FurnitureViewModel(application: Application): AndroidViewModel(application) {

    private val furnitureRepository = FurnitureRepository()
    val isLoading = MutableLiveData<Boolean?>()
    val getFurnitureResult: MutableLiveData<List<Furniture>?> = MutableLiveData()
    val getFurnitureTypesResult: MutableLiveData<List<FurnitureType>?> = MutableLiveData()
    val addFurnitureResult: MutableLiveData<AddFurnitureResultModel?> = MutableLiveData()
    val updateFurnitureResult: MutableLiveData<Furniture?> = MutableLiveData()
    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        errorMessage.value = throwable.localizedMessage
    }

    var furnitureTypes: MutableList<FurnitureType>? = null

    val dataGet: MutableLiveData<Boolean?>  = MutableLiveData()

    val getFurnitureByIdResult: MutableLiveData<FurnitureWithReviews?> = MutableLiveData()


    fun getFurnitures(token: String) {

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            isLoading.postValue(true)
            dataGet.postValue(null)
            errorMessage.postValue(null)
            var getFurnitureResponseResult: List<FurnitureModel>? = null
            try {
                getFurnitureResponseResult = furnitureRepository.getFurnitures(token)
            } catch (ex: Exception) {
                errorMessage.postValue(ex.message)
            }
            withContext(Dispatchers.Main) {
                Log.d("vmresp", getFurnitureResult.toString())
                if (getFurnitureResponseResult != null) {
                    val furnitures = mutableListOf<Furniture>()
                    getFurnitureResponseResult.forEach {
                        furnitures.add(
                            Furniture(
                                availableQuantity = it.availableQuantity,
                                id = it.id,
                                imageUrl = it.imageUrl,
                                price = it.price,
                                ratingAverage = it.ratingAverage,
                                furnitureTypeId = it.furnitureTypeId,
                                furnitureTypeName = it.furnitureTypeName,
                                description = it.description,
                                size = it.size,
                                name = it.name,
                                numberOfReviews = it.numberOfReviews,
                                discountPercentage = it.discountPercentage,
                                ordersCount = it.ordersCount,
                                isDeleted = it.isDeleted,
                                glbScale =  it.glbScale,
                                glbPath = it.glbPath
                            )
                        )
                    }
                    getFurnitureResult.value = furnitures
                    dataGet.value = true
                    isLoading.value = false
                }

            }
        }


    }

    fun furnitureList(): List<Furniture>? {
        return getFurnitureResult.value
    }

    fun getFurnitureById(token: String, id: Int) {
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            isLoading.postValue(true)
            getFurnitureByIdResult.postValue(null)
            errorMessage.postValue(null)
            var getFurnitureByIdResponseResult: FurnitureWithReviewsModel? = null
            try {
                getFurnitureByIdResponseResult = furnitureRepository.getFurnitureById(token, id)
                if (getFurnitureByIdResponseResult != null) {
                    val reviews = mutableListOf<Review>()
                    getFurnitureByIdResponseResult.reviews.forEach {
                        reviews.add(
                            Review(
                                userId = it.userId,
                                userName = it.userName,
                                date = it.date,
                                furnitureId = it.furnitureId,
                                furnitureName = it.furnitureName,
                                rating = it.rating,
                                comment = it.comment,
                                id = it.id
                            )
                        )
                    }
                    getFurnitureByIdResult.postValue(
                        FurnitureWithReviews(
                            availableQuantity = getFurnitureByIdResponseResult.availableQuantity,
                            id = getFurnitureByIdResponseResult.id,
                            imageUrl = getFurnitureByIdResponseResult.imageUrl,
                            price = getFurnitureByIdResponseResult.price,
                            ratingAverage = getFurnitureByIdResponseResult.ratingAverage,
                            furnitureTypeId = getFurnitureByIdResponseResult.furnitureTypeId,
                            furnitureTypeName = getFurnitureByIdResponseResult.furnitureTypeName,
                            description = getFurnitureByIdResponseResult.description,
                            size = getFurnitureByIdResponseResult.size,
                            name = getFurnitureByIdResponseResult.name,
                            numberOfReviews = getFurnitureByIdResponseResult.numberOfReviews,
                            discountPercentage = getFurnitureByIdResponseResult.discountPercentage,
                            ordersCount = getFurnitureByIdResponseResult.ordersCount,
                            isDeleted = getFurnitureByIdResponseResult.isDeleted,
                            reviews = reviews,
                            ordersPosition = getFurnitureByIdResponseResult.ordersPosition,
                            reviewsPosition = getFurnitureByIdResponseResult.reviewsPosition,
                            glbPath = getFurnitureByIdResponseResult.glbPath,
                            glbScale = getFurnitureByIdResponseResult.glbScale
                        )
                    )
                    isLoading.postValue(false)
                }
            } catch (ex: Exception) {
                errorMessage.postValue(ex.message)
                isLoading.postValue(false)
            }

        }
    }

        fun getFurnitureTypes(token: String) {

            CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                isLoading.postValue(true)
                getFurnitureTypesResult.postValue(null)
                var getFurnitureTypesResponseResult: List<FurnitureTypeModel>? = null
                try {
                    getFurnitureTypesResponseResult = furnitureRepository.getFurnitureTypes(token)
                    val list = mutableListOf<FurnitureType>()

                    getFurnitureTypesResponseResult!!.forEach {
                        list.add(FurnitureType(it.id, it.name))
                    }
                    furnitureTypes = list
                    getFurnitureTypesResult.postValue(list)
                } catch (ex: Exception) {
                    errorMessage.postValue(ex.message)
                }
            }
        }

    fun furnitureTypes(): List<FurnitureType>?{
        return furnitureTypes
    }

    fun addFurniture(token: String, addFurnitureModel: AddFurnitureModel) {

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            isLoading.postValue(true)

            errorMessage.postValue(null)
            var addFurnitureResponseResult: AddFurnitureResultModel? = null
            try {
                addFurnitureResponseResult = furnitureRepository.addFurniture(token, addFurnitureModel)
                addFurnitureResult.postValue(addFurnitureResponseResult)
                isLoading.postValue(false)
            } catch (ex: Exception) {
                errorMessage.postValue(ex.message)
                isLoading.postValue(false)
            }
        }
    }

    fun updateFurniture(token: String, updateFurnitureModel: UpdateFurnitureModel) {

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            isLoading.postValue(true)
            errorMessage.postValue(null)
            var updateFurnitureResponseResult: Furniture? = null
            try {
                updateFurnitureResponseResult = furnitureRepository.updateFurniture(token, updateFurnitureModel)
                updateFurnitureResult.postValue(updateFurnitureResponseResult)
                isLoading.postValue(false)
            } catch (ex: Exception) {
                errorMessage.postValue(ex.message)
                isLoading.postValue(false)
            }
        }
    }

}



