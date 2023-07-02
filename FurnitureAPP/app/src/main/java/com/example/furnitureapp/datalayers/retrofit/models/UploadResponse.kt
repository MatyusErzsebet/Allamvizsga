package com.example.furnitureapp.datalayers.retrofit.models

import com.google.gson.annotations.SerializedName

data class UploadResponse(
    @SerializedName("secure_url")
    val url: String
)
