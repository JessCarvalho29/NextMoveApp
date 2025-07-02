package com.example.nextmoveapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nextmoveapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignUp.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        binding.btnLogIn.setOnClickListener {
            val intent = Intent(this, UserLogin::class.java)
            startActivity(intent)
        }

        binding.btnSignUp2.setOnClickListener {
            val intent = Intent(this, SecondSignUp::class.java)
            startActivity(intent)
        }

        binding.btnLogIn2.setOnClickListener {
            val intent = Intent(this, SecondUserLogin::class.java)
            startActivity(intent)
        }

        binding.btnUpdates.setOnClickListener {
            val url = "https://github.blog/changelog/"
            val intent = Intent(Intent.ACTION_VIEW,  url.toUri())
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}