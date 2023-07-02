package com.example.furnitureapp.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.cloudinary.android.MediaManager
import com.example.furnitureapp.R
import com.example.furnitureapp.fragments.*
import com.example.furnitureapp.utils.Constants
import com.example.furnitureapp.viewmodels.FurnitureViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    var config: HashMap<String, String> = HashMap()
    companion object{
        var initialized = false;
    }

    //private lateinit var furnitureViewModel: FurnitureViewModel
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var furnitureViewModel: FurnitureViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        config["cloud_name"] = "djvwhh2w5"
        config["api_key"] = "544783698312274"
        config["api_secret"] = "uu4E8jvVPegnnPFy91YVW5wruIU"


        if(initialized == false) {
            MediaManager.init(this, config)
            initialized = true
        }

        //furnitureViewModel = ViewModelProvider(this)[FurnitureViewModel::class.java]

        setContentView(R.layout.activity_main)
        bottomNavigationView = findViewById(R.id.bottom_navigation_view)
        sharedPreferences = getSharedPreferences(Constants.SHAREDPREF, Context.MODE_PRIVATE)
        findViewById<RelativeLayout>(R.id.loadingPanelMain).visibility = View.GONE

        furnitureViewModel = ViewModelProvider(this)[FurnitureViewModel::class.java]

        furnitureViewModel.getFurnitureTypes(sharedPreferences.getString(Constants.TOKEN,"")!!)


        furnitureViewModel.getFurnitureTypesResult.observe(this) {
            if (it != null) {
                setOnClickListenerForNavigationItems()

                val fragmentManager = supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.mainFragmentContainerView, FurnitureListFragment())
                fragmentTransaction.commit()
                furnitureViewModel.getFurnitureTypesResult.value = null
            }

        }

        furnitureViewModel.errorMessage.observe(this){
            if(it != null)
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }

    }

    private fun setOnClickListenerForNavigationItems() {

        val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {
            val role = sharedPreferences.getString(Constants.ROLE,"")!!
            val newFragment: Fragment
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            when (it.itemId) {
                R.id.furnitureItem -> {
                    newFragment = FurnitureListFragment()
                    transaction.replace(R.id.mainFragmentContainerView, newFragment)
                    transaction.commit()
                    true
                }

                R.id.orderItem -> {

                    if(role == "User") {
                        newFragment = ShoppingCartFragment()
                        transaction.replace(R.id.mainFragmentContainerView, newFragment)
                        transaction.commit()
                    }
                    else{
                        if(role == "Admin"){
                            newFragment = AdminPurchasesFragment()
                            transaction.replace(R.id.mainFragmentContainerView, newFragment)
                            transaction.commit()
                        }
                    }
                    true
                }


                R.id.profileItem -> {
                    if(role == "User") {
                        newFragment = ProfileFragment()
                        transaction.replace(R.id.mainFragmentContainerView, newFragment)
                        transaction.commit()
                    }
                    else{
                        if(role == "Admin"){
                            newFragment = AdminUsersFragment()
                            transaction.replace(R.id.mainFragmentContainerView, newFragment)
                            transaction.commit()
                        }
                    }
                    true
                }

            }
            false
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    override fun onBackPressed() {

    }

}