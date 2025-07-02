package com.example.nextmoveapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nextmoveapp.databinding.ActivitySecondUserLoginBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SecondUserLogin : AppCompatActivity() {

    private lateinit var binding: ActivitySecondUserLoginBinding
    val currentPage = "SecondUserLogin"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySecondUserLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val firebase = Firebase.firestore

        binding.btnLogin2.setOnClickListener {
            val email = binding.emailAddressL2.text.toString().lowercase().trim()
            val password = binding.passwordL2.text.toString()
            val userCredentialsCollection = firebase.collection("userCredentials").document(email)

            if (email.isBlank()) {
                Log.d(currentPage, "email is blank")
                Toast.makeText(this, "Email cannot be empty.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Log.d(currentPage, "Invalid email format")
                Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.isBlank()) {
                Log.d(currentPage, "Password is blank")
                Toast.makeText(this, "Password cannot be empty.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            userCredentialsCollection.get()
                .addOnSuccessListener { extractedData ->
                    if (extractedData.exists()) {
                        val userData = extractedData.data
                        val storedPassword = userData?.get("password")

                        if (storedPassword == password) {
                            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                            Log.d(currentPage, "User logged in: $email")
                            val intent = Intent(this, Welcome::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, "Username or password is incorrect. Try again!", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.w(currentPage, "No such user: $email")
                    }
                }
                .addOnFailureListener { error ->
                    Toast.makeText(this, "Username or password is incorrect. Try again!", Toast.LENGTH_SHORT).show()
                    Log.e(currentPage, "Firebase error", error)
                }
        }

        binding.btnBackLogin2.setOnClickListener {
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
