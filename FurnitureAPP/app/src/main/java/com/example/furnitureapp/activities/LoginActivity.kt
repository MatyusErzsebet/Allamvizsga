package com.example.furnitureapp.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract.Constants
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import androidx.lifecycle.ViewModelProvider
import com.example.furnitureapp.R
import com.example.furnitureapp.fragments.LoginFragment
import com.example.furnitureapp.viewmodels.UserViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var userViewModel: UserViewModel
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        sharedPreferences = getSharedPreferences(com.example.furnitureapp.utils.Constants.SHAREDPREF, Context.MODE_PRIVATE)
        Log.d("token",sharedPreferences.getString(com.example.furnitureapp.utils.Constants.TOKEN,null).toString())
        sharedPreferences.edit().clear().apply()
        if(sharedPreferences.getString(com.example.furnitureapp.utils.Constants.TOKEN, null) != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        else {
            findViewById<RelativeLayout>(R.id.loadingPanel).visibility = View.GONE
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.loginFragmentContainerView, LoginFragment())
            fragmentTransaction.commit()
        }
    }
}