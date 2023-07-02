package com.example.furnitureapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.furnitureapp.R
import com.example.furnitureapp.adapters.AdminShoppingCartAdapter
import com.example.furnitureapp.adapters.ShoppingCartAdapter
import com.example.furnitureapp.utils.Constants
import com.example.furnitureapp.viewmodels.ShoppingCartViewModel


class AdminShoppingCartFragment : Fragment(), AdminShoppingCartAdapter.OnItemClickListener {

    private lateinit var shoppingCartViewModel: ShoppingCartViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var priceTextView: TextView
    private lateinit var backToOrdersButton: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shoppingCartViewModel = ViewModelProvider(requireActivity())[ShoppingCartViewModel::class.java]
    }

    @SuppressLint("MissingInflatedId", "CommitTransaction", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView =  inflater.inflate(R.layout.fragment_admin_shopping_cart, container, false)

        recyclerView = rootView.findViewById(R.id.adminShoppingCartRecyclerView)
        priceTextView = rootView.findViewById(R.id.adminShoppingCartTotalPriceTextView)
        backToOrdersButton = rootView.findViewById(R.id.adminBackToOrdersButton)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        recyclerView.adapter = AdminShoppingCartAdapter(shoppingCartViewModel.adminShoppingCart, this)

        priceTextView.text = "${shoppingCartViewModel.getAdminShoppingCartPrice()} Lei"
        backToOrdersButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.mainFragmentContainerView, AdminPurchasesFragment()).commit()
        }
        return rootView
    }

    @SuppressLint("CommitTransaction")
    override fun onItemClick(position: Int) {
        val bundle = Bundle()
        bundle.putInt(Constants.ID,(recyclerView.adapter as AdminShoppingCartAdapter).adminShoppingCartItems[position].id)
        val newFragment = FurnitureDetailsFragment()
        newFragment.arguments = bundle
        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.mainFragmentContainerView, newFragment).commit()
    }

}