package com.example.furnitureapp.models

data class FurnitureWithQuantity(
    val id: Int,
    val name: String,
    val price: Float,
    val furnitureTypeName: String,
    val description: String,
    val size: String,
    val imageUrl: String,
    val quantity: Int
)