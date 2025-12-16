package com.example.atry

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class CartActivity : AppCompatActivity() {

    private lateinit var emptyCartLayout: LinearLayout
    private lateinit var cartItemsContainer: LinearLayout
    private lateinit var tvTotalPrice: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_cart)

        // Get views
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        emptyCartLayout = findViewById(R.id.emptyCartLayout)
        cartItemsContainer = findViewById(R.id.cartItemsContainer)
        tvTotalPrice = findViewById(R.id.tvTotalPrice)

        // Back button
        btnBack.setOnClickListener {
            finish()
        }

        // Bottom navigation
        setupBottomNav()

        // Display cart items
        displayCartItems()
    }

    private fun setupBottomNav() {
        findViewById<ImageView>(R.id.btnHome).setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }

        findViewById<ImageView>(R.id.btnCart).setOnClickListener {
            // Already on cart page
        }

        findViewById<ImageView>(R.id.btnProfile).setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun displayCartItems() {
        val cartItems = CartManager.getCartItems()

        if (cartItems.isEmpty()) {
            // Show empty state
            emptyCartLayout.visibility = View.VISIBLE
            cartItemsContainer.visibility = View.GONE
            tvTotalPrice.visibility = View.GONE
        } else {
            // Show cart items
            emptyCartLayout.visibility = View.GONE
            cartItemsContainer.visibility = View.VISIBLE
            tvTotalPrice.visibility = View.VISIBLE

            // Clear container first
            cartItemsContainer.removeAllViews()

            var total = 0

            // Add each cart item
            for (item in cartItems) {
                val itemView = createCartItemView(item)
                cartItemsContainer.addView(itemView)
                total += item.price
            }

            // Update total price
            tvTotalPrice.text = "TOTAL: PHP $total"
        }
    }

    private fun createCartItemView(item: CartItem): View {
        val inflater = layoutInflater
        val itemView = inflater.inflate(R.layout.item_cart, cartItemsContainer, false)

        // Get views
        val ivImage = itemView.findViewById<ImageView>(R.id.ivCartItemImage)
        val tvName = itemView.findViewById<TextView>(R.id.tvCartItemName)
        val tvPrice = itemView.findViewById<TextView>(R.id.tvCartItemPrice)
        val btnRemove = itemView.findViewById<TextView>(R.id.btnRemoveItem)

        // Set data
        ivImage.setImageResource(item.imageRes)
        tvName.text = item.name
        tvPrice.text = "PHP ${item.price}"

        // Remove button
        btnRemove.setOnClickListener {
            CartManager.removeItem(item)
            displayCartItems()
        }

        return itemView
    }

    override fun onResume() {
        super.onResume()
        displayCartItems()
    }
}

// Cart data classes and manager
data class CartItem(
    val name: String,
    val price: Int,
    val imageRes: Int
)

data class OrderHistoryItem(
    val name: String,
    val price: Int,
    val imageRes: Int,
    val timestamp: Long = System.currentTimeMillis()
)

object CartManager {
    private val cartItems = mutableListOf<CartItem>()
    private val orderHistory = mutableListOf<OrderHistoryItem>()

    fun addItem(item: CartItem) {
        cartItems.add(item)
    }

    fun removeItem(item: CartItem) {
        cartItems.remove(item)
    }

    fun getCartItems(): List<CartItem> {
        return cartItems.toList()
    }

    fun clearCart() {
        cartItems.clear()
    }

    fun getItemCount(): Int {
        return cartItems.size
    }

    // Order history functions
    fun addToOrderHistory(items: List<CartItem>) {
        items.forEach { item ->
            orderHistory.add(OrderHistoryItem(item.name, item.price, item.imageRes))
        }
    }

    fun getRecentOrders(limit: Int = 5): List<OrderHistoryItem> {
        return orderHistory.sortedByDescending { it.timestamp }.take(limit)
    }

    fun getTopCoffee(limit: Int = 3): List<Pair<OrderHistoryItem, Int>> {
        return orderHistory
            .groupBy { it.name }
            .map { (_, items) -> items.first() to items.size }
            .sortedByDescending { it.second }
            .take(limit)
    }
}