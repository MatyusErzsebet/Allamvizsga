package com.example.furnitureapp.models

import com.google.gson.annotations.SerializedName

data class Review(
    val id: Int,
    val userId: Int,
    val userName: String,
    val furnitureName : String,
    val furnitureId: Int,
    val rating: Int,
    val comment: String?,
    val date: String
)