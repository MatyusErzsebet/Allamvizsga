package com.example.furnitureapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.furnitureapp.R
import com.example.furnitureapp.datalayers.retrofit.models.UserModel
import com.example.furnitureapp.models.Review
import com.example.furnitureapp.models.User

class UserAdapter  (var userList: MutableList<User>
) :
RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    inner class UserHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val userNameTextView = itemView.findViewById<TextView>(R.id.userListItemUserName)
        val dateTextView = itemView.findViewById<TextView>(R.id.userListItemBirthDate)
        val emailTextView = itemView.findViewById<TextView>(R.id.userListItemEmail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        lateinit var holder: RecyclerView.ViewHolder

        holder = UserHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.user_list_item, parent, false)
        )


        return holder
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val currentItem = userList[position]
        if (holder is UserHolder) {
            holder.userNameTextView.text = currentItem.firstName+" "+currentItem.lastName
            holder.emailTextView.text = currentItem.email
            holder.dateTextView.text = currentItem.birthDate.substringBefore("T")
        }


    }

    override fun getItemCount(): Int {
        return userList.size
    }
}