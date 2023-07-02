package com.example.furnitureapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.RatingBar.OnRatingBarChangeListener
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.furnitureapp.R
import com.example.furnitureapp.activities.VrActivity
import com.example.furnitureapp.adapters.FurnitureAdapter
import com.example.furnitureapp.adapters.ReviewAdapter
import com.example.furnitureapp.adapters.ShoppingCartAdapter
import com.example.furnitureapp.adapters.SimilarFurnituresAdapter
import com.example.furnitureapp.datalayers.retrofit.models.AddReviewModel
import com.example.furnitureapp.models.Furniture
import com.example.furnitureapp.models.Review
import com.example.furnitureapp.utils.Constants
import com.example.furnitureapp.viewmodels.FurnitureViewModel
import com.example.furnitureapp.viewmodels.ReviewViewModel
import com.example.furnitureapp.viewmodels.ShoppingCartViewModel


class FurnitureDetailsFragment : Fragment(), FurnitureAdapter.OnItemClickListener {

    private lateinit var furnitureItemPriceTextView: TextView
    private lateinit var ratingBar: RatingBar
    private lateinit var reviewStar: RatingBar
    private lateinit var furnitureItemImageview: ImageView
    private lateinit var furnitureItemNameTextView: TextView
    private lateinit var furnitureItemTypeTextView: TextView
    private lateinit var furnitureItemInStockTextView: TextView
    private lateinit var numberOfReviewsDetailTextView: TextView
    private lateinit var isAvailableDetailImageView: ImageView
    private lateinit var furnitureItemDiscountTextView: TextView
    private lateinit var shopButtonDetail: AppCompatButton
    private lateinit var positionInReviewsTextView: TextView
    private lateinit var positionInPurchasesTextView: TextView
    private lateinit var sizeTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var furnitureViewModel: FurnitureViewModel
    private lateinit var sharedPref: SharedPreferences
    private lateinit var progressBar: RelativeLayout
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var goToVrTextView: TextView
    private var id = 0
    private lateinit var recyclerView: RecyclerView
    private lateinit var addReviewButton: AppCompatButton
    private lateinit var reviewViewModel: ReviewViewModel
    private lateinit var reviewEditText: EditText
    private lateinit var shoppingCartViewModel: ShoppingCartViewModel
    private lateinit var furniture: Furniture
    private lateinit var similarFurnituresRecView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = requireActivity().getSharedPreferences(Constants.SHAREDPREF, Context.MODE_PRIVATE)
        furnitureViewModel = ViewModelProvider(requireActivity())[FurnitureViewModel::class.java]
        id = requireArguments().getInt(Constants.ID)
        furnitureViewModel.getFurnitureById(sharedPref.getString(Constants.TOKEN,"")!!, id)
        Log.d("iddd2", id.toString())
        reviewViewModel = ViewModelProvider(this)[ReviewViewModel::class.java]
        shoppingCartViewModel = ViewModelProvider(requireActivity())[ShoppingCartViewModel::class.java]
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_furniture_details, container, false)

        recyclerView = rootView.findViewById(R.id.reviewRecyclerView)
        progressBar = requireActivity().findViewById(R.id.loadingPanelMain)
        addReviewButton = rootView.findViewById(R.id.addReviewButton)
        reviewEditText = rootView.findViewById(R.id.reviewEditText)
        furnitureItemImageview = rootView.findViewById(R.id.furnitureItemImageview)
        furnitureItemNameTextView = rootView.findViewById(R.id.furnitureItemNameTextView)
        furnitureItemTypeTextView = rootView.findViewById(R.id.furnitureItemTypeTextView)
        furnitureItemPriceTextView = rootView.findViewById(R.id.furnitureItemPriceTextView)
        ratingBar = rootView.findViewById(R.id.detailRatingBar)
        furnitureItemInStockTextView = rootView.findViewById(R.id.furnitureItemInStockTextView)
        numberOfReviewsDetailTextView = rootView.findViewById(R.id.numberOfReviewsDetailTextView)
        goToVrTextView = rootView.findViewById(R.id.goToVrTextView)
        isAvailableDetailImageView = rootView.findViewById(R.id.isAvailableDetailImageView)
        furnitureItemDiscountTextView = rootView.findViewById(R.id.furnitureItemDiscountTextView)
        shopButtonDetail = rootView.findViewById(R.id.shopButtonDetail)
        positionInReviewsTextView = rootView.findViewById(R.id.positionInReviewsTextView)
        positionInPurchasesTextView = rootView.findViewById(R.id.positionInPurchasesTextView)
        sizeTextView = rootView.findViewById(R.id.sizeTextView)
        descriptionTextView = rootView.findViewById(R.id.descriptionTextView)
        reviewStar = rootView.findViewById(R.id.review_star)
        similarFurnituresRecView = rootView.findViewById(R.id.similarFurnituresRecyclerView)

        addOnclickListenerForAddReviewButton()
        observeGetFurnitureResponse()
        observeAddReviewResponse()
        shopButtonDetail.setOnClickListener {
            shoppingCartViewModel.addShoppingCartItem(furniture)
            Toast.makeText(requireContext(), "Added to shopping cart", Toast.LENGTH_SHORT).show()
        }


        return rootView
    }

    private fun addOnclickListenerForAddReviewButton(){
        addReviewButton.setOnClickListener {
            if(reviewStar.rating == 0.0f){
                Toast.makeText(requireContext(), "Rating is required",Toast.LENGTH_LONG).show()
            }

            else {

                val reviewTextLength = reviewEditText.length()

                Log.d("onclick","onclick")

                reviewViewModel.addReview(
                    sharedPref.getString(Constants.TOKEN, "")!!, AddReviewModel(
                        id, reviewStar.rating.toInt(),
                        if (reviewTextLength == 0) {
                            null
                        } else reviewEditText.text.toString()
                    )
                )
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeAddReviewResponse(){
        reviewViewModel.addReviewResult.observe(viewLifecycleOwner) {
            if (it != null) {
                val savedList = (recyclerView.adapter as ReviewAdapter).reviewList
                savedList.add(Review(furnitureId = it.furnitureId, furnitureName = it.furnitureName, date = it.date, rating = it.rating, userName = it.userName, comment = it.comment, id = it.id, userId = it.userId))
                reviewAdapter.reviewList = savedList
                reviewAdapter.notifyDataSetChanged()
                reviewEditText.setText("")
                reviewStar.rating = 0.0f
                Toast.makeText(requireContext(), "Review added", Toast.LENGTH_SHORT).show()
                reviewViewModel.addReviewResult.value = null

                Log.d("itt","itt")
            }
        }

        reviewViewModel.errorMessage.observe(viewLifecycleOwner) {
            if(it!= null) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                reviewViewModel.errorMessage.value=null
            }

        }

        reviewViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it != null) {
                if (it) {
                    progressBar.visibility = View.VISIBLE
                    Log.d("pb", "visible")
                } else {
                    progressBar.visibility = View.GONE
                    Log.d("pb", "gone")
                }
            }
        }
    }

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    private fun observeGetFurnitureResponse(){
                    furnitureViewModel.getFurnitureByIdResult.observe(viewLifecycleOwner){
                        if(it != null){
                            furniture = Furniture(id = it.id, name = it.name, imageUrl = it.imageUrl, price = it.price, furnitureTypeId = it.furnitureTypeId,
                            furnitureTypeName = it.furnitureTypeName, description = it.description, size = it.size, availableQuantity = it.availableQuantity, ratingAverage = it.ratingAverage,
                            numberOfReviews = it.numberOfReviews, discountPercentage = it.discountPercentage, ordersCount = it.ordersCount, isDeleted = it.isDeleted, glbPath = it.glbPath, glbScale = it.glbScale)
                            if(it.discountPercentage == 0)
                                furnitureItemDiscountTextView.visibility = View.GONE
                            else {
                                furnitureItemInStockTextView.visibility = View.VISIBLE
                                furnitureItemDiscountTextView.text = "${it.discountPercentage}%"
                            }


                            if(it.glbPath != null){
                                goToVrTextView.visibility = View.VISIBLE
                                goToVrTextView.setOnClickListener {
                                    val bundle = Bundle()
                                    bundle.putString(Constants.GLBPATH, furniture.glbPath)
                                    bundle.putFloat(Constants.GLBSCALE, furniture.glbScale)
                                    val fragment = CustomArFragment()
                                    fragment.arguments = bundle
                                    //val intent = Intent(requireActivity(), VrActivity::class.java)
                                    //intent.putExtra(Constants.GLBPATH, furniture.glbPath)
                                    //requireActivity().startActivity(intent)
                                    requireActivity().supportFragmentManager.beginTransaction().replace(R.id.mainFragmentContainerView,fragment).addToBackStack(null).commit()
                                }
                            }

                            Log.d("dptg", it.discountPercentage.toString())

                            val furnitureTypeId = it.furnitureTypeId
                furnitureItemNameTextView.text = it.name
                furnitureItemTypeTextView.text = it.furnitureTypeName
                furnitureItemPriceTextView.text = "${it.price} Lei"

                numberOfReviewsDetailTextView.text = "(${it.numberOfReviews})"
                ratingBar.rating = it.ratingAverage
                ratingBar.stepSize = 0.1f


                if(it.availableQuantity == 0){
                    furnitureItemInStockTextView.setTextColor(requireContext().resources.getColor(R.color.red))
                    furnitureItemInStockTextView.text = "Out of stock"
                    isAvailableDetailImageView.setImageResource(R.drawable.redx)
                    shopButtonDetail.isEnabled = false

                }
                else{
                    furnitureItemInStockTextView.setTextColor(requireContext().resources.getColor(R.color.available_color))
                    furnitureItemInStockTextView.text = "In stock"
                    isAvailableDetailImageView.setImageResource(R.drawable.checkicon)
                    shopButtonDetail.isEnabled = true
                }

                var rwEnding: String = when(it.reviewsPosition){
                    1 -> "st"
                    2 -> "nd"
                    3 -> "rd"
                    else -> "th"
                }

                var ordEnding: String = when(it.ordersPosition){
                    1 -> "st"
                    2 -> "nd"
                    3 -> "rd"
                    else -> "th"
                }

                sizeTextView.text = "${it.size} cm"
                descriptionTextView.text = it.description

                positionInReviewsTextView.text = "${it.reviewsPosition}${rwEnding} based on reviews"
                positionInPurchasesTextView.text = "${it.ordersPosition}${ordEnding} most purchased"

                Glide.with(requireContext()).load(it.imageUrl).into(furnitureItemImageview)

                reviewAdapter = ReviewAdapter(it.reviews.toMutableList())
                recyclerView.adapter = reviewAdapter
                recyclerView.layoutManager = LinearLayoutManager(requireContext())

                val list = furnitureViewModel.furnitureList()?.filter {
                    it.furnitureTypeId == furnitureTypeId
                }?.toMutableList()

                similarFurnituresRecView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                if(list == null) {
                    similarFurnituresRecView.adapter = SimilarFurnituresAdapter(mutableListOf(), this)
                    similarFurnituresRecView.visibility = View.GONE
                }
                else{
                    similarFurnituresRecView.adapter = SimilarFurnituresAdapter(list, this)
                }
                furnitureViewModel.getFurnitureByIdResult.value = null

            }

                        /*
            furnitureViewModel.dataGet.observe(viewLifecycleOwner) {
                if (it != null) {
                    Log.d("dataget","dataget")

                    furnitureViewModel.dataGet.value = null

                }
            }
            */


            furnitureViewModel.errorMessage.observe(viewLifecycleOwner) {
                if (it != null) {
                    reviewAdapter = ReviewAdapter(mutableListOf())
                    recyclerView.adapter = reviewAdapter
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    furnitureViewModel.errorMessage.value = null
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
    }

    override fun onItemClick(position: Int) {
        val bundle = Bundle()
        bundle.putInt(Constants.ID, (similarFurnituresRecView.adapter as SimilarFurnituresAdapter).furnitureList[position].id)
        val newFragment = FurnitureDetailsFragment()
        newFragment.arguments = bundle
        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.mainFragmentContainerView, newFragment).commit()
    }


}