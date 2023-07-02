package com.example.furnitureapp.datalayers.retrofit.models

import com.google.gson.annotations.SerializedName

data class AddFurnitureModel(
    @SerializedName("name")
    val name: String,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("price")
    val price: Float,
    @SerializedName("furnitureTypeId")
    val furnitureTypeId: Int,
    @SerializedName("description")
    val description: String,
    @SerializedName("size")
    val size: String,
    @SerializedName("availableQuantity")
    val availableQuantity: Int,
    @SerializedName("isDeleted")
    val isDeleted: Boolean
)
