package com.example.furnitureapp.models

import com.google.gson.annotations.SerializedName
import java.io.FileDescriptor

data class Furniture(
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
    val glbPath: String?,
    val glbScale: Float
)
