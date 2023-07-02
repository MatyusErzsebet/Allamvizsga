package com.example.furnitureapp.datalayers.retrofit.models

import com.google.gson.annotations.SerializedName

data class FurnitureTypeModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)
