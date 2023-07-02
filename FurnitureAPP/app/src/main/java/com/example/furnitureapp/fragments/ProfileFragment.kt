package com.example.furnitureapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.example.furnitureapp.R
import com.example.furnitureapp.activities.LoginActivity
import com.example.furnitureapp.utils.Constants
import org.w3c.dom.Text

class ProfileFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var nameTextView: TextView
    private lateinit var emailTextView: TextView
    private  lateinit var birthDateTextView: TextView
    private lateinit var logOutButton: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences(Constants.SHAREDPREF, Context.MODE_PRIVATE)

    }

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_profile, container, false)

        nameTextView = rootView.findViewById(R.id.profileUserNameTextViewContent)
        emailTextView = rootView.findViewById(R.id.profileEmailTextViewContent)
        birthDateTextView = rootView.findViewById(R.id.profileBirthDateTextViewContent)
        logOutButton = rootView.findViewById(R.id.logOutButton)

        nameTextView.text = sharedPreferences.getString(Constants.FIRSTNAME,"") + sharedPreferences.getString(Constants.LASTNAME,"")
        emailTextView.text = sharedPreferences.getString(Constants.EMAIL, "")
        birthDateTextView.text = sharedPreferences.getString(Constants.BIRTHDATE,"")?.substringBefore("T")

        logOutButton.setOnClickListener {
            sharedPreferences.edit().clear().apply()
            requireActivity().startActivity(Intent(requireActivity(), LoginActivity::class.java))
            requireActivity().finish()
        }

        return rootView
    }
}