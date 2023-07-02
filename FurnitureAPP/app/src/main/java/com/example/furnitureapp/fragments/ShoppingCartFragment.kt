package com.example.furnitureapp.fragments

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.furnitureapp.R
import com.example.furnitureapp.adapters.ShoppingCartAdapter
import com.example.furnitureapp.utils.Constants
import com.example.furnitureapp.viewmodels.ShoppingCartViewModel


class ShoppingCartFragment : Fragment(), ShoppingCartAdapter.OnItemClickListener,
    ShoppingCartAdapter.OnPlusButtonPressListener, ShoppingCartAdapter.OnMinusButtonPressListener,
    ShoppingCartAdapter.OnRemoveItemPressListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var checkOutButton: AppCompatButton
    private lateinit var totalPriceTextView: TextView
    private lateinit var shoppingCartViewModel: ShoppingCartViewModel
    private lateinit var emptyCartTextView: TextView
    private val priceLiveData: MutableLiveData<Float> = MutableLiveData()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shoppingCartViewModel = ViewModelProvider(requireActivity())[ShoppingCartViewModel::class.java]

    }

    @SuppressLint("MissingInflatedId", "SetTextI18n", "CommitTransaction")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_shopping_cart, container, false)

        recyclerView = rootView.findViewById(R.id.ordersRecyclerView)
        checkOutButton = rootView.findViewById(R.id.checkOutButton)
        totalPriceTextView = rootView.findViewById(R.id.totalPriceTextView)
        totalPriceTextView.text = "${shoppingCartViewModel.getTotalPrice()} Lei"
        emptyCartTextView = rootView.findViewById(R.id.emptyCartTextView)
        if(resources.configuration.orientation  == Configuration.ORIENTATION_PORTRAIT)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
        else
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = ShoppingCartAdapter(shoppingCartViewModel.getShoppingCartItems(), this, this, this, this)
        if(shoppingCartViewModel.getShoppingCartItems().size == 0) {
            emptyCartTextView.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            checkOutButton.visibility = View.GONE
            totalPriceTextView.visibility = View.GONE
        }

        priceLiveData.observe(viewLifecycleOwner){
            if(it == 0.0f){
                emptyCartTextView.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
                checkOutButton.visibility = View.GONE
                totalPriceTextView.visibility = View.GONE
            }
            else {
                emptyCartTextView.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                checkOutButton.visibility = View.VISIBLE
                totalPriceTextView.visibility = View.VISIBLE
                totalPriceTextView.text = "$it Lei"
            }

        }

        checkOutButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.mainFragmentContainerView, CheckOutFragment())
                .addToBackStack(null).commit()
        }

        return rootView
    }

    @SuppressLint("CommitTransaction")
    override fun onItemClick(position: Int) {
        val bundle = Bundle()
        bundle.putInt(Constants.ID, (recyclerView.adapter as ShoppingCartAdapter).shoppingCartItems[position].first.id)
        val newFragment = FurnitureDetailsFragment()
        newFragment.arguments = bundle
        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.mainFragmentContainerView, newFragment).commit()
    }

    override fun onPlusButtonClick(position: Int) {
        shoppingCartViewModel.modifyShoppingCartItem((recyclerView.adapter as ShoppingCartAdapter).shoppingCartItems[position].first.id, 1)
        priceLiveData.value = shoppingCartViewModel.getTotalPrice()
    }

    override fun onMinusButtonClick(position: Int) {
        shoppingCartViewModel.modifyShoppingCartItem((recyclerView.adapter as ShoppingCartAdapter).shoppingCartItems[position].first.id, -1)
        priceLiveData.value = shoppingCartViewModel.getTotalPrice()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onRemoveItemImageViewClick(position: Int) {
        (recyclerView.adapter as ShoppingCartAdapter).shoppingCartItems.removeAt(position)
        shoppingCartViewModel.deleteShoppingCartItem(position)
        (recyclerView.adapter as ShoppingCartAdapter).notifyDataSetChanged()
        priceLiveData.value = shoppingCartViewModel.getTotalPrice()
    }


}