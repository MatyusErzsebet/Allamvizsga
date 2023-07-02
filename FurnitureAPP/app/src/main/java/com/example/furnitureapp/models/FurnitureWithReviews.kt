package com.example.furnitureapp.models

import com.example.furnitureapp.datalayers.retrofit.models.ReviewModel
import com.google.gson.annotations.SerializedName

data class FurnitureWithReviews(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val price: Float,
    val furnitureTypeId: Int,
    val furnitureTypeName: String,
    val description: String,
    val size: String,
    val availableQuantity: Int,
    val ratingAverage: Float,
    val numberOfReviews: Int,
    val discountPercentage: Int,
    val ordersCount: Int,
    val isDeleted: Boolean,
    val reviews: List<Review>,
    val reviewsPosition: Int,
    val ordersPosition: Int,
    val glbPath: String?,
    val glbScale: Float
)

