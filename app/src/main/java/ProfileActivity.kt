package com.example.atry

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class ProfileActivity : AppCompatActivity() {

    private lateinit var recentOrdersContainer: LinearLayout
    private lateinit var topCoffeeContainer: LinearLayout
    private lateinit var emptyRecentText: TextView
    private lateinit var emptyTopText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_profile)

        // Get views
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val btnLogout = findViewById<MaterialButton>(R.id.btnLogout)
        recentOrdersContainer = findViewById(R.id.recentOrdersContainer)
        topCoffeeContainer = findViewById(R.id.topCoffeeContainer)
        emptyRecentText = findViewById(R.id.emptyRecentText)
        emptyTopText = findViewById(R.id.emptyTopText)

        // Back button
        btnBack.setOnClickListener {
            finish()
        }

        // Logout button
        btnLogout.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes") { _, _ ->


                    Toast.makeText(this, "Logged out successfully!", Toast.LENGTH_SHORT).show()

                    // Redirect to login screen
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK  // Clear activity stack for full logout
                    startActivity(intent)
                    finish()  // Close profile activity
                }
                .setNegativeButton("No", null)
                .show()
        }

        // Bottom navigation
        setupBottomNav()

        // Display orders
        displayRecentOrders()
        displayTopCoffee()
    }

    private fun setupBottomNav() {
        findViewById<ImageView>(R.id.btnHome).setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }

        findViewById<ImageView>(R.id.btnCart).setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.btnProfile).setOnClickListener {
            // Already on profile page
        }
    }

    private fun displayRecentOrders() {
        val recentOrders = CartManager.getRecentOrders(5)

        if (recentOrders.isEmpty()) {
            emptyRecentText.visibility = View.VISIBLE
            recentOrdersContainer.visibility = View.GONE
        } else {
            emptyRecentText.visibility = View.GONE
            recentOrdersContainer.visibility = View.VISIBLE
            recentOrdersContainer.removeAllViews()

            for (order in recentOrders) {
                val orderView = createSmallCoffeeCard(order.name, order.imageRes)
                recentOrdersContainer.addView(orderView)
            }
        }
    }

    private fun displayTopCoffee() {
        val topCoffee = CartManager.getTopCoffee(3)

        if (topCoffee.isEmpty()) {
            emptyTopText.visibility = View.VISIBLE
            topCoffeeContainer.visibility = View.GONE
        } else {
            emptyTopText.visibility = View.GONE
            topCoffeeContainer.visibility = View.VISIBLE
            topCoffeeContainer.removeAllViews()

            for ((coffee, count) in topCoffee) {
                val coffeeView = createSmallCoffeeCard("${coffee.name} (${count}x)", coffee.imageRes)
                topCoffeeContainer.addView(coffeeView)
            }
        }
    }

    private fun createSmallCoffeeCard(name: String, imageRes: Int): View {
        val inflater = layoutInflater
        val cardView = inflater.inflate(R.layout.item_profile_coffee, topCoffeeContainer, false)

        val ivCoffee = cardView.findViewById<ImageView>(R.id.ivProfileCoffee)
        val tvName = cardView.findViewById<TextView>(R.id.tvProfileCoffeeName)

        ivCoffee.setImageResource(imageRes)
        tvName.text = name

        return cardView
    }

    override fun onResume() {
        super.onResume()
        displayRecentOrders()
        displayTopCoffee()
    }
}
