package com.example.furnitureapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.furnitureapp.R
import com.example.furnitureapp.models.AdminPurchase
import com.example.furnitureapp.models.User

class AdminOrderAdapter(var purchaseList: MutableList<AdminPurchase>,
                        var onDetailsButtonClickListener: OnDetailsButtonClickListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnDetailsButtonClickListener {
        fun onClick(position: Int)
    }

    @SuppressLint("NotifyDataSetChanged")
    inner class PurchaseHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val userNameTextView = itemView.findViewById<TextView>(R.id.adminPurchaseListItemUserName)
        val dateTextView = itemView.findViewById<TextView>(R.id.adminPurchaseListItemDate)
        val itemCountTextView = itemView.findViewById<TextView>(R.id.adminPurchaseListItemCount)
        val addressTextView = itemView.findViewById<TextView>(R.id.adminPurchaseListAddress)
        val priceTextView = itemView.findViewById<TextView>(R.id.adminPurchaseListPrice)
        val detailsButton = itemView.findViewById<AppCompatButton>(R.id.adminDetailsButton)

        init {
            detailsButton.setOnClickListener {
                val position = layoutPosition
                onDetailsButtonClickListener.onClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        lateinit var holder: RecyclerView.ViewHolder

        holder = PurchaseHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.admin_purchase_list_item, parent, false)
        )




        return holder
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val currentItem = purchaseList[position]
        if (holder is PurchaseHolder) {
            holder.userNameTextView.text = currentItem.userName
            holder.dateTextView.text = currentItem.orderDate.substringBefore("T")
            var itemCount = 0
            currentItem.furnitures.forEach {
                itemCount+=it.quantity
            }
            holder.itemCountTextView.text = itemCount.toString()
            holder.addressTextView.text = currentItem.address
            holder.priceTextView.text = "${currentItem.finalPrice} Lei"
        }


    }

    override fun getItemCount(): Int {
        return purchaseList.size
    }
}
