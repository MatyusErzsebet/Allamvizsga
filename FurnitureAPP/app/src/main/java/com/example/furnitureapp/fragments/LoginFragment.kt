package com.example.furnitureapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.furnitureapp.activities.MainActivity
import com.example.furnitureapp.R
import com.example.furnitureapp.datalayers.repositories.UserRepository
import com.example.furnitureapp.utils.Constants
import com.example.furnitureapp.utils.observeOnce
import com.example.furnitureapp.viewmodels.UserViewModel
import com.google.android.material.textfield.TextInputLayout

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {

    private lateinit var emailEditText: EditText
    private lateinit var emailTextInputLayout: TextInputLayout
    private lateinit var passwordEditText: EditText
    private lateinit var passwordTextInputLayout: TextInputLayout
    private lateinit var loginButton: Button
    private lateinit var signUpTextView: TextView
    private lateinit var clickHereTextView: TextView
    private lateinit var userViewModel: UserViewModel
    private lateinit var progressBar: RelativeLayout
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        sharedPref =
            requireContext().getSharedPreferences(Constants.SHAREDPREF, Context.MODE_PRIVATE)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_login, container, false)

        emailEditText = rootView.findViewById(R.id.emailEditText)
        emailTextInputLayout = rootView.findViewById(R.id.emailTextInpLayout)
        passwordEditText = rootView.findViewById(R.id.passwordEditText)
        passwordTextInputLayout = rootView.findViewById(R.id.passwordTextInpLayout)
        loginButton = rootView.findViewById(R.id.loginButton)
        signUpTextView = rootView.findViewById(R.id.loginSignUp)
        clickHereTextView = rootView.findViewById(R.id.loginForgotPw)
        progressBar = requireActivity().findViewById(R.id.loadingPanel)
        progressBar.visibility = View.GONE;

        clickHereTextView.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.loginFragmentContainerView, ForgotPasswordFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        observeLoginResponse()

        setOnClickListenerForLoginButton()

        signUpTextView.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.loginFragmentContainerView, RegistrationFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        return rootView
    }

    @SuppressLint("CommitPrefEdits")
    private fun observeLoginResponse() {
        userViewModel.loginResult.observe(viewLifecycleOwner) {
            if (it != null) {
                sharedPref.edit().putString(Constants.TOKEN, it.token).apply()
                sharedPref.edit().putString(Constants.FIRSTNAME, it.user.firstName).apply()
                sharedPref.edit().putString(Constants.LASTNAME, it.user.lastName).apply()
                sharedPref.edit().putInt(Constants.ID, it.user.id).apply()
                sharedPref.edit().putString(Constants.EMAIL, it.user.email).apply()
                sharedPref.edit().putString(Constants.BIRTHDATE, it.user.birthDate).apply()
                sharedPref.edit().putString(Constants.ROLE, it.user.role).apply()

                Log.d("token", sharedPref.getString(Constants.TOKEN, "")!!)
                requireActivity().startActivity(Intent(requireActivity(), MainActivity::class.java))
                requireActivity().finish()
                userViewModel.loginResult.value = null
            }
        }

        userViewModel.errorMessage.observe(viewLifecycleOwner) {
            if(it!= null) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                userViewModel.errorMessage.value=null
            }

        }

        userViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it != null) {
                if (it) {
                    progressBar.visibility = View.VISIBLE
                    Log.d("pb", "visible")
                } else {
                    progressBar.visibility = View.GONE
                    Log.d("pb", "gone")
                }
            }
        }
    }

    private fun setOnClickListenerForLoginButton() {

        loginButton.setOnClickListener {

            //userLogin()
            adminLogin()

            emailTextInputLayout.error = ""
            passwordTextInputLayout.error = ""

            var areErrors = false

            if (emailEditText.text.isEmpty()) {
                emailTextInputLayout.error = "Please fill the email field!"
                areErrors = true
            } else {
                if (!Patterns.EMAIL_ADDRESS.matcher(emailEditText.text.toString()).matches()) {
                    emailTextInputLayout.error = "Not an email!"
                    areErrors = true
                }

            }


            if (passwordEditText.text.isEmpty()) {
                passwordTextInputLayout.error = "Please fill the password field!"
                areErrors = true
            } else {
                if (passwordEditText.text.length < 8) {
                    passwordTextInputLayout.error = "Password length must be at least 8 characters!"
                    areErrors = true
                }

            }


            if (!areErrors) {
                userViewModel.login(emailEditText.text.toString(), passwordEditText.text.toString())
            }

        }

    }

    private fun userLogin(){
        emailEditText.setText("matyusb.erzsebet@gmail.com")
        passwordEditText.setText("string12")
    }

    private fun adminLogin(){
        emailEditText.setText("matyus.erzsebet@student.ms.sapientia.ro")
        passwordEditText.setText("stringUj")
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}