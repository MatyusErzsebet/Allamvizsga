package com.example.furnitureapp.fragments

import android.os.Bundle
import android.os.Handler
import android.provider.SyncStateContract.Constants
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.furnitureapp.R
import com.example.furnitureapp.utils.observeOnce
import com.example.furnitureapp.viewmodels.UserViewModel
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern


class ForgotPasswordFragment : Fragment() {

    private lateinit var emailTextInputLayout : TextInputLayout
    private lateinit var emailEditText: EditText
    private lateinit var emailMeButton : Button
    private lateinit var userViewModel: UserViewModel
    private lateinit var progressBar: RelativeLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_forgot_password, container, false)
        emailTextInputLayout = rootView.findViewById(R.id.emailForgotPasswordTextInputLayout)
        emailEditText = rootView.findViewById(R.id.emailForgotPasswordEditText)
        emailMeButton = rootView.findViewById(R.id.emailMeButton)
        progressBar = requireActivity().findViewById(R.id.loadingPanel)

        observeSendForgotPasswordResponse()


        emailMeButton.setOnClickListener {

            emailTextInputLayout.error = ""

            var areErrors = false

            if (emailEditText.text.isEmpty()) {
                emailTextInputLayout.error = "Please fill the email field!"
                areErrors = true
            }

            else if (!Patterns.EMAIL_ADDRESS.matcher(emailEditText.text.toString()).matches()
            ) {
                emailTextInputLayout.error = "Not an email!"
                areErrors = true
            }

            if(!areErrors){

                userViewModel.sendForgotPassword(emailEditText.text.toString())
            }
        }


        return rootView
    }

    private fun observeSendForgotPasswordResponse() {

        userViewModel.sendForgotPasswordResult.observe(viewLifecycleOwner)
        {
            if (it != null && it) {
                val handler = Handler()
                val runnable = Runnable {
                    val fragmentManager = requireActivity().supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    val fragment = ValidatePasswordChangeFragment()
                    val bundle = Bundle()
                    bundle.putString(
                        com.example.furnitureapp.utils.Constants.EMAIL,
                        emailEditText.text.toString()
                    )
                    fragment.arguments = bundle
                    fragmentTransaction.replace(R.id.loginFragmentContainerView, fragment)
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                }

                handler.postDelayed(runnable, 2000)
                Toast.makeText(
                    context,
                    "Forgot password key sent",
                    Toast.LENGTH_SHORT
                ).show()
                userViewModel.sendForgotPasswordResult.value = null
            }
        }

        userViewModel.errorMessage.observe(viewLifecycleOwner) {
            if(it!=null) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                userViewModel.errorMessage.value = null
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

    override fun onDestroyView() {
        super.onDestroyView()
        userViewModel.errorMessage.removeObservers(viewLifecycleOwner)
    }


}