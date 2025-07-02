package com.example.nextmoveapp

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
import java.util.Locale

class SignUp : AppCompatActivity() {

    private lateinit var binding : ActivitySignUpBinding
    val currentPage = "SignUp"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val firebase = Firebase.firestore
        val reachUserCredentialCollection = firebase.collection("userCredentials")

        binding.btnCreateAccount.setOnClickListener {
            val username = binding.usernameInput.text.toString().lowercase()
            val password = binding.passwordInput.text.toString()
            val confirmPassword = binding.confirmPasswordInput.text.toString()

            if (password == confirmPassword) {
                var userCredentials = hashMapOf(
                    "username" to username,
                    "password" to password
                )

                reachUserCredentialCollection.document("jess").set(userCredentials)
                    .addOnSuccessListener {
                        Log.d(currentPage, "user successfully added!")
                        Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        error -> Log.w(currentPage, "Data was not send to firebase", error)
                        Toast.makeText(this, "Error creating account. Try again!", Toast.LENGTH_SHORT).show()
                    }

            } else {
                Toast.makeText(this, "Password does not match.", Toast.LENGTH_SHORT).show()
            }
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