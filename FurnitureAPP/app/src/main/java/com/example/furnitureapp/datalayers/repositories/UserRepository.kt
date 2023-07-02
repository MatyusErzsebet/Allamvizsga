package com.example.furnitureapp.datalayers.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.furnitureapp.datalayers.retrofit.RetrofitInstance
import com.example.furnitureapp.datalayers.retrofit.RetrofitInterface
import com.example.furnitureapp.datalayers.retrofit.models.BackendResponse
import com.example.furnitureapp.datalayers.retrofit.models.LoginCredentials
import com.example.furnitureapp.datalayers.retrofit.models.LoginResult
import com.example.furnitureapp.datalayers.retrofit.models.UserModel
import com.example.furnitureapp.utils.ErrorMessageHandler
import retrofit2.Response

class UserRepository {

    @Throws(Exception::class)
    suspend fun login(email: String, password: String): LoginResult {
        var loginResult: LoginResult? = null
        var errorMessage: String? = null
        val response = RetrofitInstance.login(email, password)
        Log.d("responseee", response.toString())
        if (response == null) {
            errorMessage = "Error logging in"
        } else {
            if (response.isSuccessful)
                loginResult = response.body()!!.result
            else {
                errorMessage = ErrorMessageHandler.GetErrorMessage(response.errorBody()!!)
            }
        }

        if (errorMessage != null) {
            throw Exception(errorMessage)
        }

        return loginResult!!

    }

    suspend fun registration(email: String, firstName: String, lastName: String, password:String, birthDate: String): Boolean {
        var registrationResult = false
        var errorMessage: String? = null
        val response = RetrofitInstance.registration(email, firstName, lastName, password, birthDate)
        Log.d("responseee", response.toString())
        if (response == null) {
            errorMessage = "Error sending registration code"
        } else {
            if (response.isSuccessful) {
                registrationResult = true
            }
            else {
                errorMessage = ErrorMessageHandler.GetErrorMessage(response.errorBody()!!)
            }
        }

        if (errorMessage != null) {
            throw Exception(errorMessage)
        }

        return registrationResult

    }

    suspend fun sendForgotPasswordKey(email: String): Boolean{
        var sendForgotPwResult = false
        var errorMessage: String? = null
        val response = RetrofitInstance.sendForgotPasswordKey(email)
        Log.d("responseee", response.toString())
        if (response == null) {
            errorMessage = "Error sending forgot password code"
        } else {
            if (response.isSuccessful)
                sendForgotPwResult = true
            else {
                errorMessage = ErrorMessageHandler.GetErrorMessage(response.errorBody()!!)
            }
        }

        if (errorMessage != null) {
            throw Exception(errorMessage)
        }

        return sendForgotPwResult

    }

    suspend fun verifyForgotPasswordKey(email: String, key: String): Boolean{
        var verifyForgotPwResult = false
        var errorMessage: String? = null
        val response = RetrofitInstance.verifyForgotPasswordKey(email, key)
        Log.d("responseee", response.toString())
        if (response == null) {
            errorMessage = "Error verifying forgot password code"
        } else {
            if (response.isSuccessful)
                verifyForgotPwResult = true
            else {
                errorMessage = ErrorMessageHandler.GetErrorMessage(response.errorBody()!!)
            }
        }

        if (errorMessage != null) {
            throw Exception(errorMessage)
        }

        return verifyForgotPwResult

    }

    suspend fun changePassword(email: String, password: String): Boolean{
        var changeForgotPwResult = false
        var errorMessage: String? = null
        val response = RetrofitInstance.changePassword(email, password)
        Log.d("responseee", response.toString())
        if (response == null) {
            errorMessage = "Error changing password"
        } else {
            if (response.isSuccessful)
                changeForgotPwResult = true
            else {
                errorMessage = ErrorMessageHandler.GetErrorMessage(response.errorBody()!!)
            }
        }

        if (errorMessage != null) {
            throw Exception(errorMessage)
        }

        return changeForgotPwResult

    }

    suspend fun getUsers(token: String): List<UserModel>{
        var getUsersResult: List<UserModel>? = null
        var errorMessage: String? = null
        val response = RetrofitInstance.getUsers(token)
        Log.d("responseee", response.toString())
        if (response == null) {
            errorMessage = "Error getting users"
        } else {
            if (response.isSuccessful)
                getUsersResult = response.body()!!.result
            else {
                errorMessage = ErrorMessageHandler.GetErrorMessage(response.errorBody()!!)
            }
        }

        if (errorMessage != null) {
            throw Exception(errorMessage)
        }

        return getUsersResult!!
    }
}