package com.example.furnitureapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.furnitureapp.datalayers.repositories.UserRepository
import com.example.furnitureapp.datalayers.retrofit.models.BackendResponse
import com.example.furnitureapp.datalayers.retrofit.models.LoginResult
import com.example.furnitureapp.datalayers.retrofit.models.UserModel
import com.example.furnitureapp.models.User
import kotlinx.coroutines.*
import retrofit2.Response


class UserViewModel: ViewModel() {

    private val userRepository = UserRepository()
    val isLoading = MutableLiveData<Boolean?>()
    val loginResult: MutableLiveData<LoginResult?> = MutableLiveData()
    val errorMessage: MutableLiveData<String?> = MutableLiveData()
    val registrationResult: MutableLiveData<Boolean?> = MutableLiveData()
    val sendForgotPasswordResult: MutableLiveData<Boolean?> = MutableLiveData()
    val verifySendPasswordChangeKeyResult: MutableLiveData<Boolean?> = MutableLiveData()
    val changePasswordResult: MutableLiveData<Boolean?> = MutableLiveData()
    val getUsersResult: MutableLiveData<List<User>?> = MutableLiveData()

    fun login(email: String, password: String) {
        GlobalScope.launch {
            val loginResponseResult: LoginResult?
            try {
                isLoading.postValue(true)
                errorMessage.postValue(null)

                loginResponseResult = userRepository.login(email, password)
                loginResult.postValue(loginResponseResult)
                isLoading.postValue(false)
                println("<<success")
            } catch (ex: Exception) {
                println("<<failure: ${ex.message}")
                errorMessage.postValue(ex.message)
                isLoading.postValue(false)
            }
        }

    }

    fun registration(email: String, firstName: String, lastName: String, password:String, birthDate: String){
        GlobalScope.launch {
            val registrationResponseResult: Boolean
            try {
                isLoading.postValue(true)
                errorMessage.postValue(null)

                registrationResponseResult = userRepository.registration(email, firstName, lastName, password, birthDate)
                registrationResult.postValue(registrationResponseResult)
                isLoading.postValue(false)
                println("<<success")
            } catch (ex: Exception) {
                println("<<failure: ${ex.message}")
                errorMessage.postValue(ex.message)
                isLoading.postValue(false)
            }
        }
    }

    fun sendForgotPassword(email: String){
        GlobalScope.launch {
            val forgotPwResponseResult: Boolean
            try {
                isLoading.postValue(true)
                errorMessage.postValue(null)
                forgotPwResponseResult = userRepository.sendForgotPasswordKey(email)
                sendForgotPasswordResult.postValue(forgotPwResponseResult)
                isLoading.postValue(false)
                println("<<success")
            } catch (ex: Exception) {
                println("<<failure: ${ex.message}")
                errorMessage.postValue(ex.message)
                isLoading.postValue(false)
            }
        }
    }

    suspend fun verifyPasswordChangeKey(email: String, key: String, newPassword: String){
        GlobalScope.launch {
            val verifyPwKeyResponseResult: Boolean
            try {
                isLoading.postValue(true)
                errorMessage.postValue(null)
                verifyPwKeyResponseResult = userRepository.verifyForgotPasswordKey(email, key)
                sendForgotPasswordResult.postValue(verifyPwKeyResponseResult)
                isLoading.postValue(false)
                println("<<success")

            } catch (ex: Exception) {
                println("<<failure: ${ex.message}")
                errorMessage.postValue(ex.message)
                isLoading.postValue(false)
            }
        }
        withContext(Dispatchers.Main){
            changePassword(email, newPassword)
        }
    }

    fun changePassword(email: String, password: String){
        GlobalScope.launch {
            val changePwResponseResult: Boolean
            try {
                isLoading.postValue(true)
                errorMessage.postValue(null)
                changePwResponseResult = userRepository.changePassword(email, password)
                changePasswordResult.postValue(changePwResponseResult)
                isLoading.postValue(false)
                println("<<success")
            } catch (ex: Exception) {
                println("<<failure: ${ex.message}")
                errorMessage.postValue(ex.message)
                isLoading.postValue(false)
            }
        }
    }

    fun getUsers(token: String){
        GlobalScope.launch {
            val getUsersResponseResult: List<UserModel>
            try {
                isLoading.postValue(true)
                errorMessage.postValue(null)
                getUsersResponseResult = userRepository.getUsers(token)
                val users = mutableListOf<User>()
                getUsersResponseResult.forEach {
                    users.add(User(it.id, it.email, it.firstName, it.lastName, it.birthDate, it.role))
                }
                getUsersResult.postValue(users)
                isLoading.postValue(false)
                println("<<success")
            } catch (ex: Exception) {
                println("<<failure: ${ex.message}")
                errorMessage.postValue(ex.message)
                isLoading.postValue(false)
            }
        }
    }

}
