package com.example.furnitureapp.models

import com.example.furnitureapp.datalayers.retrofit.models.FurnitureWithQuantityModel
import com.google.gson.annotations.SerializedName

data class AdminPurchase(
    val userName: String,
    val orderDate: String,
    val finalPrice: Float,
    val address: String,
    val furnitures: List<FurnitureWithQuantity>
)
