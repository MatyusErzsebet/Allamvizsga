package com.example.furnitureapp.datalayers.retrofit.models

import com.google.gson.annotations.SerializedName

data class AddPurchaseModel(
    @SerializedName("purchases")
    val purchases: List<PurchaseModel>,
    @SerializedName("address")
    val address: String
)
