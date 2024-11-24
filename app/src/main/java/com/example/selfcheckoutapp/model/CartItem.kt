package com.example.selfcheckoutapp.model

data class CartItem(
    val id: String,
    val name: String,
    val price: Double,
    val description: String,
    var quantity: Int = 1
)