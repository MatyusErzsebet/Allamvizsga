package com.example.furnitureapp.datalayers.retrofit.models

import com.google.gson.annotations.SerializedName

data class BackendResponse<T>(
    @SerializedName("result")
    val result: T?
)
