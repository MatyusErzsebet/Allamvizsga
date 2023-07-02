package com.example.furnitureapp.datalayers.retrofit.models

import com.google.gson.annotations.SerializedName

data class AdminPurchaseModel(
    @SerializedName("userName")
    val userName: String,
    @SerializedName("orderDate")
    val orderDate: String,
    @SerializedName("price")
    val finalPrice: Float,
    @SerializedName("address")
    val address: String,
    @SerializedName("furnitures")
    val furnitures: List<FurnitureWithQuantityModel>
)
