package com.example.safetycheck.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.safetycheck.databinding.ActivityHelpBinding

class Help : AppCompatActivity() {

    private lateinit var binding : ActivityHelpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.contactUs.setOnClickListener {
            startActivity(Intent(this, ContactUs::class.java))
        }

    }
}