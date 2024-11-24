package com.example.selfcheckoutapp.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.selfcheckoutapp.databinding.ItemCartBinding
import com.example.selfcheckoutapp.model.CartItem
import java.text.NumberFormat
import java.util.*

class CartAdapter(
    private val onRemoveClick: (CartItem) -> Unit
) : ListAdapter<CartItem, CartAdapter.CartViewHolder>(CartDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CartViewHolder(
        private val binding: ItemCartBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CartItem) {
            val format = NumberFormat.getCurrencyInstance(Locale.US)

            binding.apply {
                itemName.text = item.name
                itemPrice.text = format.format(item.price)
                itemQuantity.text = "Qty: ${item.quantity}"
                itemDescription.text = item.description

                // Calculate and show total for this item
                val itemTotal = item.price * item.quantity
                itemTotalPrice.text = format.format(itemTotal)  // Make sure you have this TextView in your item_cart.xml

                // Handle remove button click
                removeButton.setOnClickListener {
                    onRemoveClick(item)
                }
            }
        }
    }

    private class CartDiffCallback : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem == newItem && oldItem.quantity == newItem.quantity
        }
    }
}