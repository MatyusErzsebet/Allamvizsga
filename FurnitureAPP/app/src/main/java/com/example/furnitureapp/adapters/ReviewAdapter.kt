package com.example.furnitureapp.adapters;

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.furnitureapp.R
import com.example.furnitureapp.models.Furniture;
import com.example.furnitureapp.models.Review;

import java.util.List;

class ReviewAdapter(
        var reviewList: MutableList<Review>,
) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        @SuppressLint("NotifyDataSetChanged")
        inner class ReviewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

                private val userNameTextView = itemView.findViewById<TextView>(R.id.reviewListName)
                private val dateTextView = itemView.findViewById<TextView>(R.id.reviewListDate)
                private val ratingBar = itemView.findViewById<RatingBar>(R.id.reviewListRatingBar)
                private val commentTextView = itemView.findViewById<TextView>(R.id.reviewListComment)
                @SuppressLint("SetTextI18n")
                fun bindReview(
                       userName: String,
                       date: String,
                       rating: Int,
                       comment: String?
                ) {
                        userNameTextView.text = userName
                        dateTextView.text = date.substringBefore("T")
                        ratingBar.rating = rating.toFloat()
                        commentTextView.text = comment
                }


        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                lateinit var holder: RecyclerView.ViewHolder

                holder = ReviewHolder(
                        LayoutInflater.from(parent.context)
                                .inflate(R.layout.review_list_item, parent, false)
                )


                return holder
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

                val currentItem = reviewList[position]
                if (holder is ReviewHolder) {
                        holder.bindReview(
                                currentItem.userName,
                                currentItem.date,
                                currentItem.rating,
                                currentItem.comment
                        )

                }


        }

        override fun getItemCount(): Int {
                return reviewList.size
        }
}

