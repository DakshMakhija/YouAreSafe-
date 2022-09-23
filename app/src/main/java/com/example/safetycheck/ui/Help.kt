package com.example.safetycheck.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.safetycheck.R

class Help : AppCompatActivity() {

    private lateinit var contactUs : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)

        contactUs = findViewById(R.id.contactUs)
        contactUs.setOnClickListener {
            startActivity(Intent(this, ContactUs::class.java))
        }

    }
}