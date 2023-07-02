package com.example.furnitureapp.datalayers.retrofit.models

import com.google.gson.annotations.SerializedName

data class FurnitureWithQuantityModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("price")
    val price: Float,
    @SerializedName("furnitureTypeName")
    val furnitureTypeName: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("size")
    val size: String,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("quantity")
    val quantity: Int

)
