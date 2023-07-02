package com.example.furnitureapp.datalayers.retrofit.models

import com.google.gson.annotations.SerializedName

data class SendForgotPasswordModel(
    @SerializedName("email")
    val email: String
)
