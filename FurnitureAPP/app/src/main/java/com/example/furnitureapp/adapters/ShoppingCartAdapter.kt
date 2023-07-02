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
import com.example.furnitureapp.models.Review
import com.google.android.material.button.MaterialButton

class ShoppingCartAdapter (
    var shoppingCartItems: MutableList<Pair<Furniture, Int>>,
    private val onItemClickListener: ShoppingCartAdapter.OnItemClickListener,
    private val onPlusButtonPressListener: ShoppingCartAdapter.OnPlusButtonPressListener,
    private val onMinusButtonPressListener: ShoppingCartAdapter.OnMinusButtonPressListener,
    private val onRemoveItemPressListener: ShoppingCartAdapter.OnRemoveItemPressListener

) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    interface OnPlusButtonPressListener {
        fun onPlusButtonClick(position: Int)
    }

    interface OnMinusButtonPressListener {
        fun onMinusButtonClick(position: Int)
    }

    interface OnRemoveItemPressListener {
        fun onRemoveItemImageViewClick(position: Int)
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    inner class ShoppingCartItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val furnitureNameTextView = itemView.findViewById<TextView>(R.id.orderListItemName)
        private val furnitureTypeTextView = itemView.findViewById<TextView>(R.id.orderListItemType)
        private val imageView = itemView.findViewById<ImageView>(R.id.orderListItemImageView)
        private val originalPriceTextView = itemView.findViewById<TextView>(R.id.orderListItemOriginalPrice)
        private val priceAfterDiscountTextView = itemView.findViewById<TextView>(R.id.orderListItemPrice)
        private val sizeTextView = itemView.findViewById<TextView>(R.id.orderListItemSize)
        private val quantityTextView = itemView.findViewById<TextView>(R.id.orderItemQuantity)
        private val deleteOrderItemImageView = itemView.findViewById<ImageView>(R.id.deleteOrderItem)
        private val plusButton = itemView.findViewById<MaterialButton>(R.id.plusButton)
        private val minusButton = itemView.findViewById<MaterialButton>(R.id.minusButton)


        @SuppressLint("SetTextI18n")
        fun bindShoppingCartItem(
            furnitureName: String,
            furnitureType: String,
            imageUrl: String,
            originalPrice: Float,
            discount: Int,
            size: String,
            quantity: Int
        ) {
            furnitureNameTextView.text = furnitureName
            furnitureTypeTextView.text = furnitureType
            Glide.with(itemView.context).load(imageUrl).into(imageView)
            if(discount == 0)
                originalPriceTextView.visibility = View.GONE
            else{
                originalPriceTextView.text = "$originalPrice Lei"
            }

            priceAfterDiscountTextView.text = "${originalPrice-originalPrice*discount/100} Lei"

            sizeTextView.text = "$size cm"

            quantityTextView.text = quantity.toString()
        }

        init{

            itemView.setOnClickListener {
                val position = layoutPosition
                onItemClickListener.onItemClick(position)
            }

            minusButton.setOnClickListener {
                val position = layoutPosition
                val quantity = quantityTextView.text.toString().toInt()
                if(quantity > 1)
                    quantityTextView.text = (quantityTextView.text.toString().toInt() - 1).toString()
                onMinusButtonPressListener.onMinusButtonClick(position)
            }
            plusButton.setOnClickListener {
                val position = layoutPosition
                quantityTextView.text = (quantityTextView.text.toString().toInt() + 1).toString()
                onPlusButtonPressListener.onPlusButtonClick(position)
            }
            deleteOrderItemImageView.setOnClickListener {
                val position = layoutPosition
                onRemoveItemPressListener.onRemoveItemImageViewClick(position)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        lateinit var holder: RecyclerView.ViewHolder

        holder = ShoppingCartItemHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.order_list_item, parent, false)
        )


        return holder
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val currentItem = shoppingCartItems[position]
        if (holder is ShoppingCartItemHolder) {
            holder.bindShoppingCartItem(
               currentItem.first.name,
                currentItem.first.furnitureTypeName,
                currentItem.first.imageUrl,
                currentItem.first.price,
                currentItem.first.discountPercentage,
                currentItem.first.size,
                currentItem.second
            )
        }

    }

    override fun getItemCount(): Int {
        return shoppingCartItems.size
    }
}

