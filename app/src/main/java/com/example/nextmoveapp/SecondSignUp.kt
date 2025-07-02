package com.example.nextmoveapp

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

        binding.btnBackSignUp2.setOnClickListener {
            finish()
        }

        binding.btn2SignUp.setOnClickListener {
            val email = binding.emailAddress.text.toString().trim()
            val password = binding.passwordSU2.text.toString()
            val confirmPassword = binding.confirmPasswordSU2.text.toString()

            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val passwordPattern = Regex("^(?=.*[A-Z])(?=.*[!@#\$%^&*()_+=\\[\\]{};':\"\\\\|,.<>/?]).{8,15}\$")

            if (!passwordPattern.matches(password)) {
                Toast.makeText(this, "Password must be 8–15 characters,\n" +
                        "1 uppercase and 1 special character", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val db = Firebase.firestore
            val userCredentials = hashMapOf(
                "email" to email,
                "password" to password
            )

            db.collection("userCredentials").document(email).set(userCredentials)
                .addOnSuccessListener {
                    Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show()
                    Log.d(currentPage, "User successfully added!")
                }
                .addOnFailureListener { error ->
                    Toast.makeText(this, "Error creating account. Try again!", Toast.LENGTH_SHORT).show()
                    Log.w(currentPage, "Data was not sent to Firebase", error)
                }
        }

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }
    }
