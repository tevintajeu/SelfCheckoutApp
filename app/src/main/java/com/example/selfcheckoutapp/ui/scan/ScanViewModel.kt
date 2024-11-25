package com.example.selfcheckoutapp.ui.scan

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.selfcheckoutapp.model.CartItem

class ScanViewModel(application: Application) : AndroidViewModel(application) {
    private val _isCameraReady = MutableLiveData<Boolean>()
    val isCameraReady: LiveData<Boolean> = _isCameraReady

    private val _scannedItem = MutableLiveData<CartItem?>()
    val scannedItem: LiveData<CartItem?> = _scannedItem

    // Mock product database
    private val mockProducts = mapOf(
        "123456789" to CartItem(
            id = "123456789",
            name = "Organic Orange Juice",
            price = 4.99,
            description = "100% Natural Cold Pressed Juice"
        ),
        "987654321" to CartItem(
            id = "987654321",
            name = "Whole Grain Bread",
            price = 3.49,
            description = "Fresh Baked Artisan Bread"
        ),
        "456789123" to CartItem(
            id = "456789123",
            name = "Greek Yogurt",
            price = 2.99,
            description = "Plain Non-Fat Yogurt"
        )
    )

    fun setCameraReady(isReady: Boolean) {
        _isCameraReady.value = isReady
    }

    fun onBarcodeDetected(barcode: String) {
        val item = mockProducts[barcode] ?: CartItem(
            id = barcode,
            name = "Unknown Item",
            price = 0.0,
            description = "Product not found in database"
        )
        _scannedItem.value = item
    }

    fun clearScannedItem() {
        _scannedItem.value = null
    }
}