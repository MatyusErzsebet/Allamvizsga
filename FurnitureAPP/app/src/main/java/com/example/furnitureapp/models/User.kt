package com.example.furnitureapp.models

import com.google.gson.annotations.SerializedName

data class User(
    val id: Int,
    val email: String,
    val firstName: String,
    val lastName: String,
    val birthDate: String,
    val role: String
)
