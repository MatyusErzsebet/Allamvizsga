package com.example.furnitureapp.datalayers.retrofit.models

import com.google.gson.annotations.SerializedName

data class ReviewModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("userName")
    val userName: String,
    @SerializedName("furnitureName")
    val furnitureName : String,
    @SerializedName("furnitureId")
    val furnitureId: Int,
    @SerializedName("rating")
    val rating: Int,
    @SerializedName("comment")
    val comment: String?,
    @SerializedName("date")
    val date: String
)