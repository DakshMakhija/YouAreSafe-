package com.example.safetycheck.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.safetycheck.databinding.ActivityContactUsBinding

class ContactUs : AppCompatActivity() {

    private lateinit var binding: ActivityContactUsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactUsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivInsta.setOnClickListener {
            openApp(Uri.parse("https://www.instagram.com/axtbansal/"))
        }

        binding.ivTwitter.setOnClickListener {
            openApp(Uri.parse("https://twitter.com/AxtBansal"))
        }

        binding.ivLinkedIn.setOnClickListener {
            openApp(Uri.parse("https://www.linkedin.com/in/axtbansal/"))
        }

        binding.ivGmail.setOnClickListener {
            openApp(Uri.parse("mailto:abansal11ae@gmail.com"))
        }

        binding.ivGitHub.setOnClickListener {
            openApp(Uri.parse("https://github.com/IamBansal"))
        }

    }

    //To open a certain app on click.
    private fun openApp(uri: Uri) {
        val intent =
            Intent(Intent.ACTION_VIEW, uri)
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}