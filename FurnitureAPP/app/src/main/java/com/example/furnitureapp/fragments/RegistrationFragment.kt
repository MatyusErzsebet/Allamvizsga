package com.example.furnitureapp.fragments

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.Patterns
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.furnitureapp.R
import com.example.furnitureapp.viewmodels.UserViewModel
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*


class RegistrationFragment : Fragment() {


    private lateinit var logInTextView: TextView
    private lateinit var registerButton: Button
    private lateinit var datePicker: DatePicker
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var firstNameEditText: EditText
    private lateinit var userViewModel: UserViewModel
    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var passwordInputLayout: TextInputLayout
    private lateinit var lastNameInputLayout: TextInputLayout
    private lateinit var firstNameInputLayout: TextInputLayout
    private lateinit var progressBar: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_registration, container, false)
        logInTextView = rootView.findViewById(R.id.loginTextView)
        registerButton = rootView.findViewById(R.id.registerButton)
        emailEditText = rootView.findViewById(R.id.emailEditText)
        passwordEditText = rootView.findViewById(R.id.passwordEditText)
        lastNameEditText = rootView.findViewById(R.id.lastNameEditText)
        firstNameEditText = rootView.findViewById(R.id.firstNameEditText)
        progressBar = requireActivity().findViewById(R.id.loadingPanel)
        emailInputLayout = rootView.findViewById(R.id.emailTextInputLayout)
        passwordInputLayout = rootView.findViewById(R.id.passwordTextInputLayout)
        lastNameInputLayout = rootView.findViewById(R.id.lastNameTextInputLayout)
        firstNameInputLayout = rootView.findViewById(R.id.firstNameTextInputLayout)
        datePicker = rootView.findViewById(R.id.datePicker)

        observeRegistrationResponse()

        logInTextView.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.loginFragmentContainerView, LoginFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        setOnClickListenerForRegisterButton()

        return rootView
    }

    private fun setOnClickListenerForRegisterButton() {

        registerButton.setOnClickListener {

            emailInputLayout.error = ""
            passwordInputLayout.error = ""

            var areErrors = false


            if (emailEditText.text.isEmpty()) {
                emailInputLayout.error = "Please fill the email field!"
                areErrors = true
            } else if (!Patterns.EMAIL_ADDRESS.matcher(emailEditText.text.toString()).matches()
            ) {
                emailInputLayout.error = "Not an email!"
                areErrors = true
            }

            if (passwordEditText.text.isEmpty()) {
                passwordInputLayout.error = "Please fill the password field!"
                areErrors = true
            }

            else{
                if(passwordEditText.text.length<8){
                    passwordInputLayout.error = "Password length must be at least 8 characters"
                    areErrors = true
                }
            }

            if (lastNameEditText.text.isEmpty()) {
                lastNameInputLayout.error = "Please fill the last name field!"
                areErrors = true
            }

            if (firstNameEditText.text.isEmpty()) {
                firstNameInputLayout.error = "Please fill the first name field!"
                areErrors = true
            }

            val year: Int = datePicker.getYear()
            val month: Int = datePicker.getMonth()
            val day: Int = datePicker.getDayOfMonth()

            val calendar: Calendar = Calendar.getInstance()
            calendar.set(year, month, day)

            val format = SimpleDateFormat("yyyy-MM-dd")
            val strDate: String = format.format(calendar.time)

            if(calendar.time> Calendar.getInstance().time){
                val toast =
                    Toast.makeText(
                        context,
                        "Choose a date earlier than current date",
                        Toast.LENGTH_LONG
                    )
                toast.setGravity(Gravity.TOP, 0, 0)
                toast.show()
                areErrors = true
            }

            if (!areErrors) {
                userViewModel.registration(emailEditText.text.toString(), firstNameEditText.text.toString(), lastNameEditText.text.toString(), passwordEditText.text.toString(), strDate)
            }

        }

    }

    private fun observeRegistrationResponse(){

        userViewModel.registrationResult.observe(viewLifecycleOwner) {
            if (it!=null && it) {
                val handler = Handler()
                val runnable = Runnable {
                    val fragmentManager = requireActivity().supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.loginFragmentContainerView, LoginFragment())
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                }

                handler.postDelayed(runnable, 2000)
                Toast.makeText(
                    context,
                    "Registration code sent",
                    Toast.LENGTH_SHORT
                ).show()
                userViewModel.registrationResult.value = null
            }
        }
        userViewModel.errorMessage.observe(viewLifecycleOwner) {
            if (it != null) {
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


}