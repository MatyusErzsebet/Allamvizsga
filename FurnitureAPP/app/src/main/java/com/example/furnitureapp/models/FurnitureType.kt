package com.example.furnitureapp.models

import com.google.gson.annotations.SerializedName

data class FurnitureType(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)

