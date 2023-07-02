package com.example.furnitureapp.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.furnitureapp.datalayers.repositories.FurnitureRepository
import com.example.furnitureapp.datalayers.repositories.ReviewRepository
import com.example.furnitureapp.datalayers.retrofit.models.AddReviewModel
import com.example.furnitureapp.datalayers.retrofit.models.FurnitureModel
import com.example.furnitureapp.datalayers.retrofit.models.ReviewModel
import com.example.furnitureapp.models.Furniture
import kotlinx.coroutines.*

class ReviewViewModel: ViewModel() {
    private val reviewRepository = ReviewRepository()
    val isLoading = MutableLiveData<Boolean?>()
    val addReviewResult: MutableLiveData<ReviewModel?> = MutableLiveData()
    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        errorMessage.value = throwable.localizedMessage
    }

    fun addReview(token: String, addReviewModel: AddReviewModel){

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            var addReviewResponseResult: ReviewModel? = null
            try {
                Log.d("ittv","ittv")
                isLoading.postValue(true)
                errorMessage.postValue(null)
                addReviewResponseResult = reviewRepository.addReview(token, addReviewModel)
                Log.d("addRev", addReviewResponseResult.toString())
                addReviewResult.postValue(addReviewResponseResult)

                isLoading.postValue(false)
            } catch (ex: Exception) {
                errorMessage.postValue(ex.message)
                isLoading.postValue(false)
            }
        }

    }
}