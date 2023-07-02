package com.example.furnitureapp.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.furnitureapp.R
import com.example.furnitureapp.adapters.ReviewAdapter
import com.example.furnitureapp.datalayers.retrofit.models.AddFurnitureModel
import com.example.furnitureapp.datalayers.retrofit.models.UpdateFurnitureModel
import com.example.furnitureapp.models.Furniture
import com.example.furnitureapp.utils.Constants
import com.example.furnitureapp.viewmodels.FurnitureViewModel
import com.google.gson.Gson

class AddOrUpdateFurnitureFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var furnitureViewModel: FurnitureViewModel
    private lateinit var furnitureImageView: ImageView
    private lateinit var furnitureNameEditText: EditText
    private lateinit var furnitureTypeSpinner: Spinner
    private lateinit var sizeEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var quantityEditText: EditText
    private lateinit var discountEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var addOrUpdateFurnitureButton: AppCompatButton
    private var imageUrl: String? = null
    private var furnitureId: Int? = null
    private var furniture: Furniture? = null
    private lateinit var progressBar: RelativeLayout
    private var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences(Constants.SHAREDPREF, Context.MODE_PRIVATE)
        furnitureViewModel = ViewModelProvider(requireActivity())[FurnitureViewModel::class.java]
        furnitureId = arguments?.getInt(Constants.ID)

    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_add_or_update_furniture, container, false)

        furnitureImageView = rootView.findViewById(R.id.adminAddOrUpdateFurnitureImageView)
        furnitureNameEditText = rootView.findViewById(R.id.adminAddOrUpdateFurnitureNameEditText)
        furnitureTypeSpinner = rootView.findViewById(R.id.adminAddOrUpdateSpinner)
        sizeEditText = rootView.findViewById(R.id.adminAddOrUpdateSizeEditText)
        priceEditText = rootView.findViewById(R.id.adminAddOrUpdateFurniturePriceEditText)
        quantityEditText = rootView.findViewById(R.id.adminAddOrUpdateQuantityEditText)
        discountEditText = rootView.findViewById(R.id.adminAddOrUpdateDiscountEditText)
        descriptionEditText = rootView.findViewById(R.id.adminAddOrUpdateDescriptionTextView)
        addOrUpdateFurnitureButton = rootView.findViewById(R.id.adminAddOrUpdateFurnitureButton)
        progressBar = requireActivity().findViewById(R.id.loadingPanelMain)

        furnitureImageView.setOnClickListener {
            this.openImageChooser()
        }


        if(furnitureId != null){
            furniture = furnitureViewModel.furnitureList()!!.first{ it.id == this.furnitureId}
            Log.d("furnitureAddOrUpdate", furniture.toString())
            imageUrl = furniture!!.imageUrl
            setFurnitureDetails()

            furnitureViewModel.updateFurnitureResult.observe(viewLifecycleOwner) {
                if (it != null) {
                    Toast.makeText(
                        context,
                        "Furniture updated",
                        Toast.LENGTH_SHORT
                    ).show()
                    furnitureViewModel.updateFurnitureResult.value = null
                    addOrUpdateFurnitureButton.isEnabled = true
                }

            }


            furnitureViewModel.errorMessage.observe(viewLifecycleOwner) {
                if (it != null) {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    furnitureViewModel.errorMessage.value = null
                    addOrUpdateFurnitureButton.isEnabled = true
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


            addOrUpdateFurnitureButton.text = "Update"
            
            addOrUpdateFurnitureButton.setOnClickListener {



                if(furnitureNameEditText.text.isEmpty()) {
                    showToast("name is required")
                    return@setOnClickListener
                }

                if(sizeEditText.text.isEmpty()) {
                    showToast("size is required")
                    return@setOnClickListener
                }

                if(priceEditText.text.isEmpty()){
                    showToast("price is required")
                    return@setOnClickListener
                }
                else{
                    if(priceEditText.text.toString().toFloat() == 0.0f){
                        showToast("price can't be 0 Lei")
                        return@setOnClickListener
                    }
                }

                if(quantityEditText.text.isEmpty()){
                    showToast("quantity is required")
                    return@setOnClickListener
                }

                var discount: Int? = null

                if(discountEditText.text.isNotEmpty()){
                    discount = discountEditText.text.toString().toInt()
                    if(discount >= 100){
                        showToast("discount can't be greater than 99")
                        return@setOnClickListener
                    }
                }

                addOrUpdateFurnitureButton.isEnabled = false

                if(uri != null)
                    uploadImage(true, discount)
                else{
                    furnitureViewModel.updateFurniture(sharedPreferences.getString(Constants.TOKEN,"")!!, UpdateFurnitureModel(furniture!!.id,furnitureNameEditText.text.toString(), imageUrl!!,
                        priceEditText.text.toString().toFloat(),getFurnitureTypeIfFromSpinner() , descriptionEditText.text.toString(), sizeEditText.text.toString(), quantityEditText.text.toString().toInt(), discount, false)
                    )
                }
            }
        }

        else {
            discountEditText.isEnabled = false
            addOrUpdateFurnitureButton.text = "Add"

            val adapter = ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_dropdown_item, furnitureViewModel.furnitureTypes()!!.map { it.name })
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            furnitureTypeSpinner.adapter = adapter

            furnitureViewModel.errorMessage.observe(viewLifecycleOwner) {
                if (it != null) {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    furnitureViewModel.errorMessage.value = null
                    addOrUpdateFurnitureButton.isEnabled = true
                }
            }

            furnitureViewModel.isLoading.observe(viewLifecycleOwner) {
                if (it != null && it) {
                    progressBar.visibility = View.VISIBLE

                    Log.d("pb", "visible")
                } else {
                    progressBar.visibility = View.GONE
                    Log.d("pb", "gone")
                }
            }

            furnitureViewModel.addFurnitureResult.observe(viewLifecycleOwner) {
                if (it != null) {
                    val handler = Handler()
                    val runnable = Runnable {
                        val fragmentManager = requireActivity().supportFragmentManager
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.replace(
                            R.id.mainFragmentContainerView,
                            FurnitureListFragment()
                        )
                        fragmentTransaction.addToBackStack(null)
                        fragmentTransaction.commit()
                    }

                    handler.postDelayed(runnable, 2000)
                    Toast.makeText(
                        context,
                        "Furniture added",
                        Toast.LENGTH_SHORT
                    ).show()

                    addOrUpdateFurnitureButton.isEnabled = true
                    furnitureViewModel.addFurnitureResult.value = null
                }
            }


            addOrUpdateFurnitureButton.setOnClickListener {

                if (uri == null) {
                    showToast("image is required")
                    return@setOnClickListener
                }
                if (furnitureNameEditText.text.isEmpty()) {
                    showToast("name is required")
                    return@setOnClickListener
                }

                if (sizeEditText.text.isEmpty()) {
                    showToast("size is required")
                    return@setOnClickListener
                }

                if (priceEditText.text.isEmpty()) {
                    showToast("price is required")
                    return@setOnClickListener
                } else {
                    if (priceEditText.text.toString().toFloat() == 0.0f) {
                        showToast("price can't be 0 Lei")
                        return@setOnClickListener
                    }
                }

                if (quantityEditText.text.isEmpty()) {
                    showToast("quantity is required")
                    return@setOnClickListener
                }

                if (descriptionEditText.text.isEmpty()) {
                    showToast("description is required")
                    return@setOnClickListener
                }

                addOrUpdateFurnitureButton.isEnabled = false

                if(uri == null) {
                    furnitureViewModel.addFurniture(
                        sharedPreferences.getString(Constants.TOKEN, "")!!, AddFurnitureModel(
                            furnitureNameEditText.text.toString(),
                            imageUrl!!,
                            priceEditText.text.toString().toFloat(),
                            getFurnitureTypeIfFromSpinner(),
                            descriptionEditText.text.toString(),
                            sizeEditText.text.toString(),
                            quantityEditText.text.toString().toInt(),
                            false
                        )
                    )
                }

                else {
                    uploadImage(false, null)
                }
            }
        }

        return rootView
    }

    @SuppressLint("SetTextI18n")
    private fun setFurnitureDetails(){
        Glide.with(requireContext()).load(furniture!!.imageUrl).into(furnitureImageView)
        furnitureNameEditText.setText(furniture!!.name)
        sizeEditText.setText(furniture!!.size)
        val adapter = ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_dropdown_item, furnitureViewModel.furnitureTypes()!!.map { it.name })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        furnitureTypeSpinner.adapter = adapter
        sizeEditText.setText(furniture!!.size)
        priceEditText.setText(furniture!!.price.toString())
        quantityEditText.setText(furniture!!.availableQuantity.toString())
        if(furniture!!.discountPercentage != 0)
            discountEditText.setText(furniture!!.discountPercentage.toString())
        descriptionEditText.setText(furniture!!.description)

    }
    
    private fun showToast(message: String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun getFurnitureTypeIfFromSpinner(): Int{
        return furnitureViewModel.furnitureTypes!!.first { it.name == furnitureTypeSpinner.selectedItem as String }.id
    }

    fun openImageChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            uri = data?.data
            if (uri != null) {
                furnitureImageView.setImageURI(uri)
            }
        }
    }

    private fun uploadImage(update: Boolean, discount: Int?) {

        MediaManager.get().upload(uri)
            .callback(object : UploadCallback {
                override fun onStart(requestId: String) {

                }

                override fun onProgress(
                    requestId: String,
                    bytes: Long,
                    totalBytes: Long
                ) {

                }

                override fun onSuccess(
                    requestId: String,
                    resultData: Map<*, *>?
                ) {

                    var secure_url = resultData?.get("secure_url") as String
                    if (update == false) {
                        furnitureViewModel.addFurniture(
                            sharedPreferences.getString(Constants.TOKEN, "")!!, AddFurnitureModel(
                                furnitureNameEditText.text.toString(),
                                secure_url,
                                priceEditText.text.toString().toFloat(),
                                getFurnitureTypeIfFromSpinner(),
                                descriptionEditText.text.toString(),
                                sizeEditText.text.toString(),
                                quantityEditText.text.toString().toInt(),
                                false
                            )
                        )

                    }
                    else{
                        furnitureViewModel.updateFurniture(sharedPreferences.getString(Constants.TOKEN,"")!!, UpdateFurnitureModel(furniture!!.id,furnitureNameEditText.text.toString(), imageUrl!!,
                            priceEditText.text.toString().toFloat(),getFurnitureTypeIfFromSpinner() , descriptionEditText.text.toString(), sizeEditText.text.toString(), quantityEditText.text.toString().toInt(), discount, false)
                        )
                    }
                }

                override fun onError(
                    requestId: String,
                    error: ErrorInfo
                ) {
                    Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()
                }

                override fun onReschedule(
                    requestId: String,
                    error: ErrorInfo
                ) {

                }
            }).dispatch()
    }
}