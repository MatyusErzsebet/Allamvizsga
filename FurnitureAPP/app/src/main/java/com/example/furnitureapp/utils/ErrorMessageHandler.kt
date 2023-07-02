package com.example.furnitureapp.utils

import androidx.annotation.Nullable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import org.json.JSONObject

object ErrorMessageHandler {
    fun GetErrorMessage(@Nullable responseBody: ResponseBody): String{
        val jsonObj = JSONObject(responseBody.charStream().readText())
        return jsonObj.getString("result")
    }
}