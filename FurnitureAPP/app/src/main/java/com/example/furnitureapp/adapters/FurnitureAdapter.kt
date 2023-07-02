package com.example.furnitureapp.adapters;

import android.annotation.SuppressLint
import android.graphics.Color
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
import org.w3c.dom.Text

class FurnitureAdapter(
    var furnituresList: MutableList<Furniture>,
    val onItemClickListener: OnItemClickListener
    ) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        interface OnItemClickListener {
            fun onItemClick(position: Int)
        }



        @SuppressLint("NotifyDataSetChanged")
        inner class FurnitureHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                var id = 0
                var furnitureImageView: ImageView = itemView.findViewById<ImageView>(R.id.furnitureListItemImageview)
                private val furnitureNameTextView = itemView.findViewById<TextView>(R.id.furnitureListItemNameTextView)
                private val numberOfReviewsTextView = itemView.findViewById<TextView>(R.id.numberOfReviewsTextView)
                private val furnitureTypeTextView = itemView.findViewById<TextView>(R.id.furnitureListItemTypeTextView)
                private val priceTextView = itemView.findViewById<TextView>(R.id.furnitureListItemPriceTextView)
                private val discountTextView = itemView.findViewById<TextView>(R.id.furnitureListItemDiscountTextView)
                val ratingBar = itemView.findViewById<RatingBar>(R.id.listRatingBar)
                private val inStockTextView = itemView.findViewById<TextView>(R.id.furnitureListItemInStockTextView)
                private val availabilityImageView = itemView.findViewById<ImageView>(R.id.isAvailableImageView)


                @SuppressLint("SetTextI18n")
                fun bindFurniture(
                    idP: Int, furnitureName: String, furnitureTypeName: String, price: Float, ratingAverage: Float, numberOfReviews: Int, discount: Int, availableQuantity: Int)
                {
                    this.id = idP
                    furnitureNameTextView.text = furnitureName

                    furnitureTypeTextView.text = furnitureTypeName
                    priceTextView.text = "$price Lei"
                    numberOfReviewsTextView.text = "($numberOfReviews)"
                    ratingBar.rating = ratingAverage
                    ratingBar.stepSize = 0.1f
                    if(discount == 0)
                        discountTextView.visibility = View.GONE
                    else
                        discountTextView.text = "$discount%"

                    if(availableQuantity == 0){
                        inStockTextView.setTextColor(itemView.context.resources.getColor(R.color.red))
                        inStockTextView.text = "Out of stock"
                        availabilityImageView.setImageResource(R.drawable.redx)
                    }
                    else{
                        inStockTextView.setTextColor(itemView.context.resources.getColor(R.color.available_color))
                        inStockTextView.text = "In stock"
                        availabilityImageView.setImageResource(R.drawable.checkicon)
                    }

                }

            init {
                itemView.setOnClickListener {
                    val position = layoutPosition
                    onItemClickListener.onItemClick(position)
                }
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                lateinit var holder: RecyclerView.ViewHolder

                holder = FurnitureHolder(
                        LayoutInflater.from(parent.context)
                                .inflate(R.layout.furniture_list_item, parent, false)
                )


                return holder
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

                val currentItem = furnituresList[position]
                if (holder is FurnitureHolder) {
                        holder.bindFurniture(
                                currentItem.id,
                                currentItem.name,
                                currentItem.furnitureTypeName,
                                currentItem.price,
                                currentItem.ratingAverage,
                                currentItem.numberOfReviews,
                                currentItem.discountPercentage,
                                currentItem.availableQuantity
                        )

                    Glide.with(holder.itemView.context)
                        .load(currentItem.imageUrl)
                        .into(holder.furnitureImageView)
                }



        }

        override fun getItemCount(): Int {
                return furnituresList.size
        }
}
