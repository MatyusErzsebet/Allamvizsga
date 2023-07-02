package com.example.furnitureapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.furnitureapp.R
import com.example.furnitureapp.models.Furniture
import com.example.furnitureapp.models.FurnitureWithQuantity
import com.example.furnitureapp.models.Review
import com.google.android.material.button.MaterialButton

class AdminShoppingCartAdapter (
    var adminShoppingCartItems: MutableList<FurnitureWithQuantity>,
    val onItemClickListener: OnItemClickListener

) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    inner class AdminShoppingCartItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val furnitureNameTextView = itemView.findViewById<TextView>(R.id.adminOrderListItemName)
        private val furnitureTypeTextView = itemView.findViewById<TextView>(R.id.adminOrderListItemType)
        private val imageView = itemView.findViewById<ImageView>(R.id.adminOrderListItemImageView)
        private val priceTextView = itemView.findViewById<TextView>(R.id.adminOrderListItemPrice)
        private val sizeTextView = itemView.findViewById<TextView>(R.id.adminOrderListItemSize)
        private val quantityTextView = itemView.findViewById<TextView>(R.id.adminOrderItemQuantity)

        init{
            itemView.setOnClickListener {
                onItemClickListener.onItemClick(layoutPosition)
            }
        }

        @SuppressLint("SetTextI18n")
        fun bindShoppingCartItem(
            furnitureName: String,
            furnitureType: String,
            imageUrl: String,
            price: Float,
            size: String,
            quantity: Int
        ) {
            furnitureNameTextView.text = furnitureName
            furnitureTypeTextView.text = furnitureType
            Glide.with(itemView.context).load(imageUrl).into(imageView)

            sizeTextView.text = "$size cm"

            priceTextView.text = "${price/quantity} Lei"
            quantityTextView.text = quantity.toString()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        lateinit var holder: RecyclerView.ViewHolder

        holder = AdminShoppingCartItemHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.admin_shopping_cart_item, parent, false)
        )


        return holder
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val currentItem = adminShoppingCartItems[position]
        if (holder is AdminShoppingCartItemHolder) {
            holder.bindShoppingCartItem(
                currentItem.name,
                currentItem.furnitureTypeName,
                currentItem.imageUrl,
                currentItem.price,
                currentItem.size,
                currentItem.quantity
            )
        }

    }

    override fun getItemCount(): Int {
        return adminShoppingCartItems.size
    }
}

