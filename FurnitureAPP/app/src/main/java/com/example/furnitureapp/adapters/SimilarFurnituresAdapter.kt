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

class SimilarFurnituresAdapter(

    var furnitureList: MutableList<Furniture>,
    val onItemClickListener: FurnitureAdapter.OnItemClickListener
    ) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

        @SuppressLint("NotifyDataSetChanged")
        inner class FurnitureHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val imageView = itemView.findViewById<ImageView>(R.id.similarListItemImageView)
            val textView = itemView.findViewById<TextView>(R.id.similarListItemName)

            init{
                itemView.setOnClickListener {
                    onItemClickListener.onItemClick(layoutPosition)
                }
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            lateinit var holder: RecyclerView.ViewHolder

            holder = FurnitureHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.similar_furniture_list_item, parent, false)
            )


            return holder
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            val currentItem = furnitureList[position]
            if (holder is FurnitureHolder) {
                holder.textView.text = currentItem.name
                Glide.with(holder.itemView.context).load(currentItem.imageUrl).into(holder.imageView)
            }

        }

        override fun getItemCount(): Int {
            return furnitureList.size
        }
    }