package com.example.atry

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_register)

        // Get references to input fields, buttons, and text
        val usernameInput = findViewById<TextInputEditText>(R.id.etUsername)
        val passwordInput = findViewById<TextInputEditText>(R.id.etPassword)
        val loginText = findViewById<TextView>(R.id.tvLogin)
        val logInWithButton = findViewById<MaterialButton>(R.id.btnLogInWith)
        val signUpButton = findViewById<MaterialButton>(R.id.btnSignUp)

        // Login text click - go back to login page
        loginText.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Close register activity
        }

        // Log in with button click - go back to login page
        logInWithButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Close register activity
        }

        // Sign Up button click - validate inputs, save to SharedPreferences, then go back to login page
        signUpButton.setOnClickListener {
            val username = usernameInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()


            // Validate inputs
            when {
                username.isEmpty() -> {
                    Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show()
                }
                password.isEmpty() -> {
                    Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // Save username and password to SharedPreferences
                    val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putString("username", username)
                        putString("password", password)
                        apply()  // Save asynchronously
                    }

                    Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show()

                    // Redirect to login page
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}