package com.example.furnitureapp.datalayers.retrofit.models

import com.google.gson.annotations.SerializedName

data class FurnitureModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("price")
    val price: Float,
    @SerializedName("furnitureTypeId")
    val furnitureTypeId: Int,
    @SerializedName("furnitureTypeName")
    val furnitureTypeName: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("size")
    val size: String,
    @SerializedName("availableQuantity")
    val availableQuantity: Int,
    @SerializedName("ratingAverage")
    val ratingAverage: Float,
    @SerializedName("numberOfReviews")
    val numberOfReviews: Int,
    @SerializedName("discountPercentage")
    val discountPercentage: Int,
    @SerializedName("ordersCount")
    val ordersCount: Int,
    @SerializedName("isDeleted")
    val isDeleted: Boolean,
    @SerializedName("glbPath")
    val glbPath: String?,
    @SerializedName("glbScale")
    val glbScale: Float
)