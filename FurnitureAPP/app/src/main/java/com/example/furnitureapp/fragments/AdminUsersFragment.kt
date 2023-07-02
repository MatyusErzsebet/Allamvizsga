package com.example.furnitureapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.SyncStateContract.Constants
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.furnitureapp.R
import com.example.furnitureapp.activities.LoginActivity
import com.example.furnitureapp.adapters.ReviewAdapter
import com.example.furnitureapp.adapters.UserAdapter
import com.example.furnitureapp.viewmodels.UserViewModel


class AdminUsersFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userViewModel: UserViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: RelativeLayout
    private lateinit var logOutButton: AppCompatButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences(com.example.furnitureapp.utils.Constants.SHAREDPREF, Context.MODE_PRIVATE)
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        userViewModel.getUsers(sharedPreferences.getString(com.example.furnitureapp.utils.Constants.TOKEN,"")!!)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_admin_users, container, false)

        progressBar = requireActivity().findViewById(R.id.loadingPanelMain)
        logOutButton = rootView.findViewById(R.id.adminLogOutButton)
        recyclerView = rootView.findViewById(R.id.usersRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        userViewModel.getUsersResult.observe(viewLifecycleOwner){
            if(it != null) {
                recyclerView.adapter = UserAdapter(it.toMutableList())
                userViewModel.errorMessage.value = null
            }
        }

        userViewModel.errorMessage.observe(viewLifecycleOwner) {
            if (it != null) {
                recyclerView.adapter = UserAdapter(mutableListOf())
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                userViewModel.errorMessage.value = null
            }
        }

        userViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it!=null && it) {
                progressBar.visibility = View.VISIBLE

                Log.d("pb", "visible")
            } else {
                progressBar.visibility = View.GONE
                Log.d("pb", "gone")
            }
        }

        logOutButton.setOnClickListener {
            sharedPreferences.edit().clear().apply()
            requireActivity().startActivity(Intent(requireActivity(), LoginActivity::class.java))
            requireActivity().finish()
        }

        return rootView
    }

}