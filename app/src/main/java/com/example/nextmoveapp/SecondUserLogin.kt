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

        val db = Firebase.firestore
        val userCredentialsCollection = db.collection("userCredentials")

        binding.btnLogin2.setOnClickListener {
            val email = binding.emailAddressL2.text.toString().trim()
            val password = binding.passwordL2.text.toString()

            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val passwordPattern = Regex("^(?=.*[A-Z])(?=.*[!@#\$%^&*()_+=\\[\\]{};':\"\\\\|,.<>/?]).{8,15}\$")
            if (!passwordPattern.matches(password)) {
                Toast.makeText(this, "Password must be 8–15 chars, 1 uppercase, 1 special char", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            userCredentialsCollection.document(email).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val userData = document.data
                        val storedPassword = userData?.get("password")

                        if (storedPassword == password) {
                            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                            Log.d(currentPage, "User logged in: $email")
                            val intent = Intent(this, Welcome::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
                        Log.w(currentPage, "No such user: $email")
                    }
                }
                .addOnFailureListener { error ->
                    Toast.makeText(this, "Login failed. Try again.", Toast.LENGTH_SHORT).show()
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
