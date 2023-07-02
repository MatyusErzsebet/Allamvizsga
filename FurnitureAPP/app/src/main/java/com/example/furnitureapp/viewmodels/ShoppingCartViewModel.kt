package com.example.furnitureapp.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.furnitureapp.datalayers.repositories.ShoppingCartRepository
import com.example.furnitureapp.datalayers.retrofit.models.*
import com.example.furnitureapp.models.AdminPurchase
import com.example.furnitureapp.models.Furniture
import com.example.furnitureapp.models.FurnitureWithQuantity
import com.example.furnitureapp.models.User
import kotlinx.coroutines.*

class ShoppingCartViewModel: ViewModel() {
    val placeOrderResult: MutableLiveData<String?> = MutableLiveData()
    val getAllPurchasesResult: MutableLiveData<List<AdminPurchase>?> = MutableLiveData()
    val shoppingCartRepository = ShoppingCartRepository()
    private val furnitures: MutableList<Pair<Furniture, Int>> = mutableListOf()
    val errorMessage: MutableLiveData<String?> = MutableLiveData()
    val isLoading = MutableLiveData<Boolean?>()
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        errorMessage.value = throwable.localizedMessage
    }

    var adminShoppingCart: MutableList<FurnitureWithQuantity> = mutableListOf()

    fun getShoppingCartItems() : MutableList<Pair<Furniture, Int>>{
        return furnitures
    }

    fun addShoppingCartItem(furniture: Furniture){
        var result: Pair<Furniture, Int>? = null
        var position: Int? = null
        if(furnitures.size > 0) {
            for (i in 0 until furnitures.size) {
                if (furnitures[i].first.id == furniture.id) {
                    result = furnitures[i]
                    position = i
                    break
                }
            }
        }

        if( result == null)
            furnitures.add(Pair(furniture, 1))
        else {
            furnitures[position!!.toInt()] = Pair<Furniture, Int>(first = result.first, second = result.second+1)
        }



        Log.d("cartList", furnitures.toString())
    }

    fun deleteShoppingCartItem(id: Int){
        furnitures.remove(furnitures.firstOrNull {
            it.first.id == id
        })
    }

    fun modifyShoppingCartItem(id: Int, quantity: Int){
        var result: Pair<Furniture, Int>? = null
        var position: Int? = null
        for (i in 0..furnitures.size){
            if(furnitures[i].first.id == id){
                result = furnitures[i]
                position = i
                break
            }
        }

        if( result != null)
            furnitures[position!!.toInt()] = Pair<Furniture, Int>(first = result.first, second = result.second+quantity)

    }

    fun getTotalPrice(): Float{
        var price = 0.0f
        furnitures.forEach {
            price+=(it.first.price-it.first.price * it.first.discountPercentage/100) * it.second
        }

        return price
    }

    fun placeOrder(token: String, address: String){
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            var addOrderResponseResult: String? = null
            try {
                Log.d("ittv","ittv")
                isLoading.postValue(true)
                errorMessage.postValue(null)
                val purchases = createPurchaseModel()
                val addPurchaseModel = AddPurchaseModel(purchases, address)
                addOrderResponseResult = shoppingCartRepository.addPurchase(token, addPurchaseModel )
                Log.d("addRev", addOrderResponseResult.toString())
                placeOrderResult.postValue(addOrderResponseResult)

                isLoading.postValue(false)
            } catch (ex: Exception) {
                errorMessage.postValue(ex.message)
                isLoading.postValue(false)
            }
        }
    }

    private fun createPurchaseModel(): List<PurchaseModel>{
        val result = mutableListOf<PurchaseModel>()
        furnitures.forEach {
            result.add(PurchaseModel(it.first.id, it.second))
        }

        return result.toList()
    }

    fun getAllPurchases(token: String){
        GlobalScope.launch {
            val getAllPurchasesResponseResult: List<AdminPurchaseModel>
            try {
                isLoading.postValue(true)
                errorMessage.postValue(null)
                getAllPurchasesResponseResult = shoppingCartRepository.gettAllPurchases(token)
                val purchases = mutableListOf<AdminPurchase>()
                getAllPurchasesResponseResult.forEach {
                    val furnitures = mutableListOf<FurnitureWithQuantity>()
                    it.furnitures.forEach {
                        furnitures.add(FurnitureWithQuantity(id = it.id, name = it.name, price = it.price, furnitureTypeName = it.furnitureTypeName,
                        description = it.description, size = it.size, imageUrl =  it.imageUrl, quantity = it.quantity))
                    }

                    purchases.add(AdminPurchase(userName = it.userName, orderDate = it.orderDate, address = it.address, finalPrice = it.finalPrice, furnitures = furnitures.toList()))

                }
                getAllPurchasesResult.postValue(purchases)
                isLoading.postValue(false)
                println("<<success")
            } catch (ex: Exception) {
                println("<<failure: ${ex.message}")
                errorMessage.postValue(ex.message)
                isLoading.postValue(false)
            }
        }
    }

    fun getAdminShoppingCartPrice(): Float{
        var sum = 0.0f
        adminShoppingCart.forEach {
            sum += it.price
        }
        return sum
    }

}