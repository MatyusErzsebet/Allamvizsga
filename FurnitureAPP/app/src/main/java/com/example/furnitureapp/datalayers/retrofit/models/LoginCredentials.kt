package com.example.furnitureapp.datalayers.retrofit.models

import com.google.gson.annotations.SerializedName

data class LoginCredentials(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)
