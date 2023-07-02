package com.example.furnitureapp.datalayers.retrofit.models

import com.google.gson.annotations.SerializedName

data class LoginResult(
    @SerializedName("user")
    val user: UserModel,
    @SerializedName("token")
    val token: String
)
