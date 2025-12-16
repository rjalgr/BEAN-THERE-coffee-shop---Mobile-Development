package com.example.atry

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.button.MaterialButton

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_dashboard)

        // Get all coffee cards
        val mochaCard = findViewById<CardView>(R.id.cardMocha)
        val latteCard = findViewById<CardView>(R.id.cardLatte)
        val cortadoCard = findViewById<CardView>(R.id.cardCortado)
        val affogatoCard = findViewById<CardView>(R.id.cardAffogato)
        val icedCoffeeCard = findViewById<CardView>(R.id.cardIcedCoffee)
        val macchiatoCard = findViewById<CardView>(R.id.cardMacchiato)

        // Add hover effect and click listeners
        setupCard(mochaCard, "Mocha", "PHP: 130",
            "A delicious blend of espresso, steamed milk, and chocolate.", R.drawable.mocha)
        setupCard(latteCard, "Latte", "PHP: 120",
            "Latte means milk in Italian — so caffè latte literally means coffee with milk.", R.drawable.latte)
        setupCard(cortadoCard, "Cortado", "PHP: 110",
            "A cortado is a Spanish coffee drink made with equal parts espresso and steamed milk.", R.drawable.cortado)
        setupCard(affogatoCard, "Affogato", "PHP: 150",
            "An Italian dessert made with espresso poured over vanilla ice cream.", R.drawable.affogato)
        setupCard(icedCoffeeCard, "Iced Coffee", "PHP: 100",
            "Cold brewed coffee served over ice, perfect for warm days.", R.drawable.iced_coffee)
        setupCard(macchiatoCard, "Macchiato", "PHP: 115",
            "Espresso with a small amount of foamed milk on top.", R.drawable.macchiato)

        // Bottom navigation - Cart button
        findViewById<ImageView>(R.id.btnCart).setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

        // Bottom navigation - Profile button
        findViewById<ImageView>(R.id.btnProfile).setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupCard(card: CardView, name: String, price: String, description: String, imageRes: Int) {
        // Add hover effect
        card.setOnTouchListener { view, event ->
            when (event.action) {
                android.view.MotionEvent.ACTION_DOWN -> {
                    animateCard(view, 0.95f, 100)
                }
                android.view.MotionEvent.ACTION_UP,
                android.view.MotionEvent.ACTION_CANCEL -> {
                    animateCard(view, 1.0f, 100)
                }
            }
            false
        }

        // Add click listener to show dialog
        card.setOnClickListener {
            showCoffeeDialog(name, price, description, imageRes)
        }
    }

    private fun animateCard(view: View, scale: Float, duration: Long) {
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", scale)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", scale)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleX, scaleY)
        animatorSet.duration = duration
        animatorSet.start()
    }

    private fun showCoffeeDialog(name: String, price: String, description: String, imageRes: Int) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_coffee_detail)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // Get dialog views
        val ivCoffeeImage = dialog.findViewById<ImageView>(R.id.ivCoffeeImage)
        val tvCoffeeName = dialog.findViewById<TextView>(R.id.tvCoffeeName)
        val tvCoffeePrice = dialog.findViewById<TextView>(R.id.tvCoffeePrice)
        val tvCoffeeDescription = dialog.findViewById<TextView>(R.id.tvCoffeeDescription)
        val btnBack = dialog.findViewById<ImageView>(R.id.btnBack)
        val btnCart = dialog.findViewById<ImageView>(R.id.btnCartDialog)
        val btnOrderNow = dialog.findViewById<MaterialButton>(R.id.btnOrderNow)

        // Set data
        ivCoffeeImage.setImageResource(imageRes)
        tvCoffeeName.text = name.uppercase()
        tvCoffeePrice.text = price
        tvCoffeeDescription.text = description

        // Set back icon
        btnBack.setImageResource(R.drawable.ic_back)

        // Back button closes dialog
        btnBack.setOnClickListener {
            dialog.dismiss()
        }

        // Cart button - Add to cart
        btnCart.setOnClickListener {
            val priceValue = price.replace("PHP: ", "").toInt()
            val cartItem = CartItem(name, priceValue, imageRes)
            CartManager.addItem(cartItem)
            Toast.makeText(this, "Added $name to cart!", Toast.LENGTH_SHORT).show()
        }

        // Order Now button
        btnOrderNow.setOnClickListener {
            // Add to order history
            val priceValue = price.replace("PHP: ", "").toInt()
            val orderItem = CartItem(name, priceValue, imageRes)
            CartManager.addToOrderHistory(listOf(orderItem))

            Toast.makeText(this, "Order placed for $name!", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.show()
    }
}