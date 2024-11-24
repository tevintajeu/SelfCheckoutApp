package com.example.selfcheckoutapp.ui.scan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.selfcheckoutapp.databinding.BottomSheetScannedItemBinding
import com.example.selfcheckoutapp.model.CartItem
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.NumberFormat
import java.util.*

class ScannedItemBottomSheet : BottomSheetDialogFragment() {
    private var _binding: BottomSheetScannedItemBinding? = null
    private val binding get() = _binding!!

    private var onAddToCart: ((CartItem) -> Unit)? = null
    private var cartItem: CartItem? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetScannedItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cartItem?.let { item ->
            val format = NumberFormat.getCurrencyInstance(Locale.US)

            binding.apply {
                itemName.text = item.name
                itemPrice.text = format.format(item.price)
                itemDescription.text = item.description

                quantityMinusButton.setOnClickListener {
                    val currentQty = quantityText.text.toString().toInt()
                    if (currentQty > 1) {
                        quantityText.text = (currentQty - 1).toString()
                    }
                }

                quantityPlusButton.setOnClickListener {
                    val currentQty = quantityText.text.toString().toInt()
                    quantityText.text = (currentQty + 1).toString()
                }

                addToCartButton.setOnClickListener {
                    val quantity = quantityText.text.toString().toInt()
                    item.quantity = quantity
                    onAddToCart?.invoke(item)
                    dismiss()
                }

                cancelButton.setOnClickListener {
                    dismiss()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(item: CartItem, onAddToCart: (CartItem) -> Unit) =
            ScannedItemBottomSheet().apply {
                this.cartItem = item
                this.onAddToCart = onAddToCart
            }
    }
}