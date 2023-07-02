package com.example.furnitureapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.furnitureapp.R
import com.example.furnitureapp.utils.Constants
import com.example.furnitureapp.viewmodels.UserViewModel
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ValidatePasswordChangeFragment: Fragment() {

    private lateinit var codeEditText: EditText
    private lateinit var newPasswordEditText: EditText
    private lateinit var updateButton: Button
    private lateinit var codeInputLayout: TextInputLayout
    private lateinit var newPasswordInputLayout: TextInputLayout
    private lateinit var userViewModel: UserViewModel
    private lateinit var progressBar: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
    }


    @SuppressLint("CutPasteId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_validate_password_change, container, false)
        codeEditText = rootView.findViewById(R.id.codeChangePasswordEditText)
        codeInputLayout = rootView.findViewById(R.id.codeChangePasswordTextInputLayout)
        newPasswordEditText = rootView.findViewById(R.id.newPasswordEditText)
        newPasswordInputLayout = rootView.findViewById(R.id.newPasswordTextInputLayout)
        updateButton = rootView.findViewById(R.id.updatePwButton)
        progressBar = requireActivity().findViewById(R.id.loadingPanel)

        observeKeyValidationResponse()
        observePasswordChangeResponse()


        setOnClickListenerForRegisterButton()

        return rootView
    }


    @SuppressLint("SimpleDateFormat")
    private fun setOnClickListenerForRegisterButton() {

        updateButton.setOnClickListener {

            codeInputLayout.error = ""
            newPasswordInputLayout.error = ""

            var areErrors = false


            if (codeEditText.text.isEmpty()) {
                codeInputLayout.error = "Please fill the code field!"
                areErrors = true
            } else if (codeEditText.text.length<8)
             {
                codeInputLayout.error = "Code length must be 8 characters!"
                areErrors = true
            }

            if (newPasswordEditText.text.isEmpty()) {
                newPasswordInputLayout.error = "Please fill the password field!"
                areErrors = true
            } else if (codeEditText.text.length<8)
            {
                codeInputLayout.error = "Password length must be at least 8 characters!"
                areErrors = true
            }

            if (!areErrors) {
                GlobalScope.launch {
                    userViewModel.verifyPasswordChangeKey(
                        requireArguments().getString(Constants.EMAIL)!!,
                        codeEditText.text.toString(),
                        newPasswordEditText.text.toString()
                    )
                }
            }

        }

    }

    private fun observeKeyValidationResponse(){
        userViewModel.verifySendPasswordChangeKeyResult.observe(viewLifecycleOwner) {
            if (it != null && it) {
                userViewModel.changePassword(
                    requireArguments().getString(Constants.EMAIL)!!,
                    newPasswordEditText.text.toString()
                )
                userViewModel.verifySendPasswordChangeKeyResult.value = null
            }
        }
        userViewModel.errorMessage.observe(viewLifecycleOwner) {
            if(it != null) {
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

    private fun observePasswordChangeResponse(){
        userViewModel.changePasswordResult.observe(viewLifecycleOwner) {
            if (it!= null && it) {
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
                    "Password changed",
                    Toast.LENGTH_SHORT
                ).show()
                userViewModel.changePasswordResult.value = null
            }
        }
        userViewModel.errorMessage.observe(viewLifecycleOwner) {
            if(it!=null) {
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
