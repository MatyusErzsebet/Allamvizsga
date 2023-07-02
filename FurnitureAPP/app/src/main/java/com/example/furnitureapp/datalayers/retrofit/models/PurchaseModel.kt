package com.example.furnitureapp.datalayers.retrofit.models

import com.google.gson.annotations.SerializedName

data class PurchaseModel(
    @SerializedName("furnitureId")
    val furnitureId: Int,
    @SerializedName("quantity")
    val quantity: Int

)
