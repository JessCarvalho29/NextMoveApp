package com.example.nextmoveapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nextmoveapp.databinding.ActivitySignUpBinding
import android.util.Log
import android.widget.Toast
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore

class SignUp : AppCompatActivity() {

    private lateinit var binding : ActivitySignUpBinding
    val currentPage = "SignUp"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val firebase = Firebase.firestore
        Log.d(currentPage, "Firestore instance: $firebase")
        val reachUserCredentialCollection = firebase.collection("userCredentials")

        binding.btnCreateAccount.setOnClickListener {
            Log.d(currentPage, "Button clicked")
            val username = binding.usernameInput.text.toString().lowercase()
            val password = binding.passwordInput.text.toString()
            val confirmPassword = binding.confirmPasswordInput.text.toString()

            Log.d(currentPage, "username=$username, password=$password, confirmPassword=$confirmPassword")

            if (username.isBlank()) {
                Log.d(currentPage, "Username is blank")
                Toast.makeText(this, "Username cannot be empty.", Toast.LENGTH_SHORT).show()
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

            Log.d(currentPage, "Passing validation, about to save in Firestore")

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userCredential = hashMapOf(
                "username" to username,
                "password" to password,
            )

            reachUserCredentialCollection.document(username).set(userCredential)
                //.addOnSuccessListener {
                    Log.d(currentPage, "User successfully added to Firestore")
                    Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, UserLogin::class.java)
                    startActivity(intent)
                //}
                //.addOnFailureListener {
                    //error -> Log.w(currentPage, "Failed to save to Firestore", error)
                    //Toast.makeText(this, "Error creating account. Try again!", Toast.LENGTH_SHORT).show()
                //}
        }

        binding.btnBackFromSignUp.setOnClickListener {
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}