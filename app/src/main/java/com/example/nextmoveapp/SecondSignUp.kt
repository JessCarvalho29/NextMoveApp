package com.example.nextmoveapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nextmoveapp.databinding.ActivitySecondSignUpBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class SecondSignUp : AppCompatActivity() {

    private lateinit var binding: ActivitySecondSignUpBinding
    val currentPage = "secondSignUp"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySecondSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val firebase = Firebase.firestore

        binding.btn2SignUp.setOnClickListener {
            val email = binding.emailAddress.text.toString().lowercase().trim()
            val password = binding.passwordSU2.text.toString()
            val confirmPassword = binding.confirmPasswordSU2.text.toString()
            val reachUserCredential = firebase.collection("userCredentials").document(email)

            if (email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Log.d(currentPage, "Invalid email")
                Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.isBlank()) {
                Log.d(currentPage, "Password is blank")
                Toast.makeText(this, "Password cannot be empty.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (confirmPassword.isBlank()) {
                Log.d(currentPage, "Passwords do not match")
                Toast.makeText(this, "Confirm password cannot be empty.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val passwordPattern = Regex("^(?=.*[A-Z])(?=.*[!@#\$%^&*()_+=\\[\\]{};':\"\\\\|,.<>/?]).{8,15}\$")

            if (!passwordPattern.matches(password)) {
                Toast.makeText(
                    this, "Password must be 8–15 characters,\n" +
                            "1 uppercase and 1 special character", Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userCredentials = hashMapOf(
                "username" to email,
                "password" to password
            )

            reachUserCredential.get()
                .addOnSuccessListener { extractedData ->
                    if (extractedData.exists() && extractedData.getString("username") == email) {
                        Toast.makeText(
                            this,
                            "Email already exists. Please choose another or log in.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } }
                .addOnFailureListener {
                    try {
                        reachUserCredential.set(userCredentials)
                        Toast.makeText(
                            this,
                            "Account created successfully!",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d(currentPage, "User successfully added to Firestore!")
                        val intent = Intent(this, SecondUserLogin::class.java)
                        startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(
                            this,
                            "Error creating account. Try again!",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.w(currentPage, "Data was not sent to Firebase", e)
                    }
                }
        }

        binding.btnBackSignUp2.setOnClickListener {
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }
}