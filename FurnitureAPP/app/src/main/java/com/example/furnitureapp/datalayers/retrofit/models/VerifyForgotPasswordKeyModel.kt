package com.example.furnitureapp.datalayers.retrofit.models

import com.google.gson.annotations.SerializedName

data class VerifyForgotPasswordKeyModel(
    @SerializedName("email")
    val email: String,
    @SerializedName("key")
    val key: String
)
