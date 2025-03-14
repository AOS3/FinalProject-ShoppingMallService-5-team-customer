package com.judamie_user.android.viewmodel.componentviewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CartIdCountViewModel  : ViewModel() {
    private val _cartMap = MutableLiveData<HashMap<String, Int>>()
    val cartMap: LiveData<HashMap<String, Int>> get() = _cartMap

    fun addToCart(productId: String, quantity: Int) {
        val currentCart = _cartMap.value ?: HashMap()
        currentCart[productId] = quantity
        _cartMap.value = currentCart
    }
}