package com.example.furnitureapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.SyncStateContract.Constants
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.furnitureapp.R
import com.example.furnitureapp.adapters.AdminOrderAdapter
import com.example.furnitureapp.adapters.UserAdapter
import com.example.furnitureapp.models.AdminPurchase
import com.example.furnitureapp.models.Furniture
import com.example.furnitureapp.viewmodels.ShoppingCartViewModel


class AdminPurchasesFragment : Fragment(), AdminOrderAdapter.OnDetailsButtonClickListener {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var shoppingCartViewModel: ShoppingCartViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: RelativeLayout
    private lateinit var searchView: androidx.appcompat.widget.SearchView
    private lateinit var purchases: MutableList<AdminPurchase>
    private lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences(com.example.furnitureapp.utils.Constants.SHAREDPREF, Context.MODE_PRIVATE)
        shoppingCartViewModel = ViewModelProvider(requireActivity())[ShoppingCartViewModel::class.java]
        shoppingCartViewModel.getAllPurchases(sharedPreferences.getString(com.example.furnitureapp.utils.Constants.TOKEN,"")!!)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_admin_purchases, container, false)

        progressBar = requireActivity().findViewById(R.id.loadingPanelMain)
        recyclerView = rootView.findViewById(R.id.purchasesRecyclerViewAdmin)
        searchView = rootView.findViewById(R.id.purchasesListSearchView)
        spinner = rootView.findViewById(R.id.dropStatusPurchases)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        shoppingCartViewModel.getAllPurchasesResult.observe(viewLifecycleOwner){
            if(it != null) {
                val list = it.toMutableList()
                list.sortByDescending { it.orderDate }
                purchases = list
                recyclerView.adapter = AdminOrderAdapter(list, this)
                setOnSearchViewTextChangedListener()
                setItemsForSpinner()
                shoppingCartViewModel.errorMessage.value = null
            }
        }

        shoppingCartViewModel.errorMessage.observe(viewLifecycleOwner) {
            if (it != null) {
                recyclerView.adapter = UserAdapter(mutableListOf())
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                shoppingCartViewModel.errorMessage.value = null
            }
        }

        shoppingCartViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it!=null && it) {
                progressBar.visibility = View.VISIBLE

                Log.d("pb", "visible")
            } else {
                progressBar.visibility = View.GONE
                Log.d("pb", "gone")
            }
        }


        return rootView
    }

    private fun setOnSearchViewTextChangedListener(){

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String): Boolean {

                (recyclerView.adapter as AdminOrderAdapter).purchaseList = searchResult(newText)
                (recyclerView.adapter as AdminOrderAdapter).notifyDataSetChanged()
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }

        })
    }

    private fun searchResult(userNameP: String) : MutableList<AdminPurchase>{

        val result = purchases.filter {
            it.userName.contains(userNameP,ignoreCase = true)
        }

        return result!!.toMutableList()
    }

    private fun setItemsForSpinner() {

        val list: MutableList<String> = ArrayList()
        list.add("select one")
        list.add("Ascending by customer")
        list.add("Descending by customer")
        list.add("Ascending by price")
        list.add("Descending by price")
        list.add("Ascending by date")
        list.add("Descending by date")

        val adapter = ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_dropdown_item,list)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onItemSelected(
                arg0: AdapterView<*>?,
                arg1: View?,
                arg2: Int,
                arg3: Long
            ) {

                when (arg2) {
                    1 -> purchases.sortBy { it.userName.lowercase() }
                    2 -> purchases.sortByDescending { it.userName.lowercase() }
                    3 -> purchases.sortBy { it.finalPrice }
                    4 -> purchases.sortByDescending { it.finalPrice }
                    5 -> purchases.sortBy { it.orderDate }
                    6 -> purchases.sortByDescending { it.orderDate }
                }

                (recyclerView.adapter as AdminOrderAdapter).purchaseList = purchases
                (recyclerView.adapter as AdminOrderAdapter).notifyDataSetChanged()


            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onNothingSelected(arg0: AdapterView<*>?) {

            }
        }
    }

    override fun onClick(position: Int) {
        shoppingCartViewModel.adminShoppingCart = (recyclerView.adapter as AdminOrderAdapter).purchaseList[position].furnitures.toMutableList()
        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.mainFragmentContainerView, AdminShoppingCartFragment()).commit()
    }

}