package com.example.nextmoveapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nextmoveapp.databinding.ActivityUserLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class UserLogin : AppCompatActivity() {

    private lateinit var binding: ActivityUserLoginBinding
    val currentPage = "Login"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityUserLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val firebase = Firebase.firestore
        val reachUserCredentialCollection = firebase.collection("userCredentials")

        binding.btnLogIntoAccount.setOnClickListener {
            val username = binding.usernameInputLogin.text.toString()
            val password = binding.passwordInputLogin.text.toString()

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

            reachUserCredentialCollection.document(username).get()
                .addOnSuccessListener {
                        extractedData ->
                    if (extractedData.exists()){
                        var userCredentials = extractedData.data
                        Log.d(currentPage, "Data extracted successfully ${extractedData.id} : ${extractedData.data}")

                        if (userCredentials?.get("username") == username && userCredentials["password"] == password){
                            val intent = Intent(this, Welcome::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, "Username or password is incorrect. Try again!", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else {
                        Log.w(currentPage, "No data to extract")
                    }
                }
                .addOnFailureListener { error -> Log.e(currentPage, "Error to extract data from firebase", error) }
        }

        binding.btnLogIntoAccount.setOnClickListener {
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}