package com.example.furnitureapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import com.example.furnitureapp.R
import com.example.furnitureapp.utils.Constants
import com.example.furnitureapp.viewmodels.ShoppingCartViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class CheckOutFragment : Fragment() {

    private lateinit var totalPriceTextView:  TextView
    private lateinit var shoppingCartViewModel: ShoppingCartViewModel
    private lateinit var streetTextInputLayout: TextInputLayout
    private lateinit var streetEditText: TextInputEditText
    private lateinit var nrTextInputLayout: TextInputLayout
    private lateinit var nrEditText: TextInputEditText
    private lateinit var cityTextInputLayout: TextInputLayout
    private lateinit var cityEditText: TextInputEditText
    private lateinit var regionTextInputLayout: TextInputLayout
    private lateinit var regionEditText: TextInputEditText
    private lateinit var countryTextInputLayout: TextInputLayout
    private lateinit var countryEditText: TextInputEditText
    private lateinit var placeOrderButton: AppCompatButton
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var progressBar: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shoppingCartViewModel = ViewModelProvider(requireActivity())[ShoppingCartViewModel::class.java]
        sharedPreferences = requireActivity().getSharedPreferences(Constants.SHAREDPREF, Context.MODE_PRIVATE)
    }

    @SuppressLint("SetTextI18n", "CutPasteId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView =  inflater.inflate(R.layout.fragment_check_out, container, false)

        progressBar = requireActivity().findViewById(R.id.loadingPanelMain)
        totalPriceTextView = rootView.findViewById(R.id.totalPriceCheckOutTextView)
        countryTextInputLayout = rootView.findViewById(R.id.countryTextInpLayout)
        countryEditText = rootView.findViewById(R.id.countryEditText)
        streetTextInputLayout = rootView.findViewById(R.id.streetTextInpLayout)
        streetEditText = rootView.findViewById(R.id.streetEditText)
        nrTextInputLayout = rootView.findViewById(R.id.nrTextInpLayout)
        nrEditText = rootView.findViewById(R.id.nrEditText)
        regionTextInputLayout = rootView.findViewById(R.id.regionTextInpLayout)
        regionEditText = rootView.findViewById(R.id.regionEditText)
        cityTextInputLayout = rootView.findViewById(R.id.cityTextInpLayout)
        cityEditText = rootView.findViewById(R.id.cityEditText)
        placeOrderButton = rootView.findViewById(R.id.finalizePurchaseButton)

        totalPriceTextView.text = "${shoppingCartViewModel.getTotalPrice()} Lei"

        observePlaceOrderResult()
        setOnClickListenerForPurchaseButton()

        return rootView
    }

    private fun setOnClickListenerForPurchaseButton() {

        placeOrderButton.setOnClickListener {

            streetTextInputLayout.error = ""
            nrTextInputLayout.error = ""
            cityTextInputLayout.error = ""
            regionTextInputLayout.error = ""
            countryTextInputLayout.error = ""

            var areErrors = false

            if(streetEditText.text != null){
                if(streetEditText.text!!.isEmpty()) {
                    areErrors = true
                    streetTextInputLayout.error = "Street required"
                }
            }
            else {
                areErrors = true
            }

            if(nrEditText.text != null){
                if(nrEditText.text!!.isEmpty()) {
                    areErrors = true
                    nrTextInputLayout.error = "Number required"
                }
            }
            else {
                areErrors = true
            }

            if(cityEditText.text != null){
                if(cityEditText.text!!.isEmpty()) {
                    areErrors = true
                    cityTextInputLayout.error = "City required"
                }
            }
            else {
                areErrors = true
            }

            if(regionEditText.text != null){
                if(regionEditText.text!!.isEmpty()) {
                    areErrors = true
                    regionTextInputLayout.error = "Region required"
                }
            }
            else {
                areErrors = true
            }

            if(countryEditText.text != null){
                if(countryEditText.text!!.isEmpty()) {
                    areErrors = true
                    countryTextInputLayout.error = "Country required"
                }
            }
            else {
                areErrors = true
            }


            if (!areErrors) {
                val address = streetEditText.text.toString() + ", "+nrEditText.text.toString() + ", "+cityEditText.text.toString() + ", " + regionEditText.text.toString() + ", " + countryEditText.text.toString()
                shoppingCartViewModel.placeOrder(sharedPreferences.getString(Constants.TOKEN, "")!!, address )
            }

        }

    }

    private fun observePlaceOrderResult(){
        shoppingCartViewModel.placeOrderResult.observe(viewLifecycleOwner){
            if (it != null) {

                val fragment = FurnitureListFragment()

                val handler = Handler()
                val runnable = Runnable {
                    requireActivity().supportFragmentManager.beginTransaction().replace(R.id.mainFragmentContainerView, fragment).commit()
                }

                handler.postDelayed(runnable, 3000)
                Toast.makeText(
                    context,
                    "Order placed successfully",
                    Toast.LENGTH_SHORT
                ).show()
                shoppingCartViewModel.placeOrderResult.value = null
                shoppingCartViewModel.adminShoppingCart.clear()
            }
        }

        shoppingCartViewModel.errorMessage.observe(viewLifecycleOwner) {
            if(it!= null) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                shoppingCartViewModel.errorMessage.value=null
            }

        }

        shoppingCartViewModel.isLoading.observe(viewLifecycleOwner) {
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