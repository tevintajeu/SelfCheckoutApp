package com.example.selfcheckoutapp.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.selfcheckoutapp.model.CartItem

class CartViewModel : ViewModel() {
    private val _cartItems = MutableLiveData<List<CartItem>>()
    val cartItems: LiveData<List<CartItem>> = _cartItems

    private val _total = MutableLiveData<Double>()
    val total: LiveData<Double> = _total

    init {
        _cartItems.value = emptyList()
        _total.value = 0.0
    }

    fun addItem(item: CartItem) {
        val currentItems = _cartItems.value?.toMutableList() ?: mutableListOf()
        val existingItem = currentItems.find { it.id == item.id }

        if (existingItem != null) {
            existingItem.quantity++
        } else {
            currentItems.add(item)
        }

        _cartItems.value = currentItems
        calculateTotal()
    }

    fun removeItem(item: CartItem) {
        val currentItems = _cartItems.value?.toMutableList() ?: mutableListOf()
        currentItems.removeAll { it.id == item.id }
        _cartItems.value = currentItems
        calculateTotal()
    }

    private fun calculateTotal() {
        val total = _cartItems.value?.sumOf { it.price * it.quantity } ?: 0.0
        _total.value = total
    }
}