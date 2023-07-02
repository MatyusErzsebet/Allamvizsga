package com.example.furnitureapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.furnitureapp.R
import com.example.furnitureapp.adapters.FurnitureAdapter
import com.example.furnitureapp.models.Furniture
import com.example.furnitureapp.utils.Constants
import com.example.furnitureapp.viewmodels.FurnitureViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class FurnitureListFragment : Fragment(), FurnitureAdapter.OnItemClickListener {
    private lateinit var furnitureViewModel: FurnitureViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var sharedPref: SharedPreferences
    private lateinit var progressBar: RelativeLayout
    private lateinit var furnitureAdapter: FurnitureAdapter
    private lateinit var fragmentContainerView: FragmentContainerView
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var searchView: androidx.appcompat.widget.SearchView
    private lateinit var furnitures: MutableList<Furniture>
    private lateinit var spinner: Spinner
    private lateinit var floatingActionButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        furnitureViewModel = ViewModelProvider(requireActivity())[FurnitureViewModel::class.java]
        sharedPref = requireContext().getSharedPreferences(Constants.SHAREDPREF, Context.MODE_PRIVATE)
        furnitures = mutableListOf()

        furnitureViewModel.getFurnitures(sharedPref.getString(Constants.TOKEN,"")!!)

    }

    @SuppressLint("MissingInflatedId", "CommitTransaction")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_furniture_list, container, false)
        Log.d("ocw1", "asd")

        observeGetFurnituresResponse()

        searchView = rootView.findViewById(R.id.furnitureListSearchView)
        spinner = rootView.findViewById(R.id.dropStatus)
        recyclerView = rootView.findViewById(R.id.furnitureRecyclerView)
        bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        fragmentContainerView = requireActivity().findViewById<FragmentContainerView>(R.id.mainFragmentContainerView)
        progressBar = requireActivity().findViewById(R.id.loadingPanelMain)
        floatingActionButton = rootView.findViewById(R.id.addFurnitureFloatingActionButton)

        if(sharedPref.getString(Constants.ROLE, "") == "Admin") {
            floatingActionButton.visibility = View.VISIBLE
            floatingActionButton.setOnClickListener {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.mainFragmentContainerView, AddOrUpdateFurnitureFragment())
                    .commit()
            }
        }

        else
            floatingActionButton.visibility = View.INVISIBLE

        return rootView
    }

    private fun setOnSearchViewTextChangedListener(){

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String): Boolean {

                furnitures = searchResult(newText)
                furnitureAdapter.furnituresList = furnitures
                furnitureAdapter.notifyDataSetChanged()
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }

        })
    }

    private fun searchResult(titleP: String) : MutableList<Furniture>{

        val result = furnitureViewModel.furnitureList()?.filter {
            it.name.contains(titleP,ignoreCase = true)
        }

        return result!!.toMutableList()
    }

    private fun setItemsForSpinner() {

        val list: MutableList<String> = ArrayList()
        list.add("select one")
        list.add("Ascending by name")
        list.add("Descending by name")
        list.add("Ascending by price")
        list.add("Descending by price")
        list.add("Ascending by rating")
        list.add("Descending by rating")
        list.add("Ascending by sales")
        list.add("Descending by sales")

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
                    1 -> furnitures.sortBy { it.name.lowercase() }
                    2 -> furnitures.sortByDescending { it.name.lowercase() }
                    3 -> furnitures.sortBy { it.price }
                    4 -> furnitures.sortByDescending { it.price }
                    5 -> furnitures.sortBy { it.ratingAverage }
                    6 -> furnitures.sortByDescending { it.ratingAverage }
                    7 -> furnitures.sortBy { it.ordersCount }
                    8 -> furnitures.sortByDescending { it.ordersCount }
                }

                furnitureAdapter.furnituresList = furnitures
                furnitureAdapter.notifyDataSetChanged()


            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        }
    }

    private fun observeGetFurnituresResponse(){
        furnitureViewModel.getFurnitureResult.observe(viewLifecycleOwner) {
            if (it != null) {
                Log.d("count1234", it!!.count().toString())
                val list = it.toMutableList()
                list.shuffle()
                furnitures = list
                furnitureAdapter = FurnitureAdapter(list,this)
                recyclerView.adapter = furnitureAdapter
                if(resources.configuration.orientation  == Configuration.ORIENTATION_PORTRAIT)
                    recyclerView.layoutManager = LinearLayoutManager(requireContext())
                else
                    recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

                setOnSearchViewTextChangedListener()
                setItemsForSpinner()
            }
            else {
                furnitureAdapter = FurnitureAdapter(mutableListOf<Furniture>(), this)
            }
        }

        furnitureViewModel.errorMessage.observe(viewLifecycleOwner) {
            if (it != null) {
                furnitureAdapter = FurnitureAdapter(mutableListOf<Furniture>(), this)
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        furnitureViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it!=null && it) {
                progressBar.visibility = View.VISIBLE

                Log.d("pb", "visible")
            } else {
                progressBar.visibility = View.GONE
                Log.d("pb", "gone")
            }
        }
    }

//    override fun onConfigurationChanged(newConfig: Configuration) {
//        super.onConfigurationChanged(newConfig)
//        furnitureAdapter = FurnitureAdapter(furnitureViewModel.getFurnitureList()!!.toMutableList(), this)
//    }


    override fun onItemClick(position: Int) {
        if (sharedPref.getString(Constants.ROLE, "") == "User") {
            val bundle = Bundle()
            bundle.putInt(Constants.ID, furnitureAdapter.furnituresList[position].id)
            Log.d("idd", furnitureAdapter.furnituresList[position].id.toString())
            val fragment = FurnitureDetailsFragment()
            fragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.mainFragmentContainerView, fragment).addToBackStack(null).commit()
            //requireActivity().startActivity(Intent(requireActivity(),VrActivity::class.java))
        }

        if (sharedPref.getString(Constants.ROLE, "") == "Admin") {
            val bundle = Bundle()
            bundle.putInt(Constants.ID, furnitureAdapter.furnituresList[position].id)
            Log.d("idd", furnitureAdapter.furnituresList[position].id.toString())
            val fragment = AddOrUpdateFurnitureFragment()
            fragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.mainFragmentContainerView, fragment).addToBackStack(null).commit()
            //requireActivity().startActivity(Intent(requireActivity(),VrActivity::class.java))
        }
    }

    /*
    override fun onDestroyView() {
        super.onDestroyView()
        val layoutParams = fragmentContainerView.layoutParams as ViewGroup.MarginLayoutParams
        bottomNavigationView.visibility = View.VISIBLE
        layoutParams.bottomMargin = 60
        fragmentContainerView.layoutParams = layoutParams
        Log.d("odw","odw")
    }
    */


    override fun onDetach() {
        super.onDetach()
        Log.d("detach", "detach")
    }

}