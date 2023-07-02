package com.example.furnitureapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.furnitureapp.R
import com.example.furnitureapp.fragments.CustomArFragment

class VrActivity : AppCompatActivity() {

    private lateinit var arFragment: CustomArFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vr)

        arFragment = supportFragmentManager.findFragmentById(R.id.arFragment) as CustomArFragment
    }

    override fun onBackPressed() {
        startActivity(Intent(this,MainActivity::class.java))
    }
}
