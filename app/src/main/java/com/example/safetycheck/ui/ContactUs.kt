package com.example.safetycheck.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.safetycheck.R

class ContactUs : AppCompatActivity() {

    private lateinit var ig: ImageView
    private lateinit var gmail: ImageView
    private lateinit var twitter: ImageView
    private lateinit var linkedIn: ImageView
    private lateinit var gitHub: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)

        ig = findViewById(R.id.ivInsta)
        gmail = findViewById(R.id.ivGmail)
        twitter = findViewById(R.id.ivTwitter)
        linkedIn = findViewById(R.id.ivLinkedIn)
        gitHub = findViewById(R.id.ivGitHub)

        ig.setOnClickListener {
            openApp(Uri.parse("https://www.instagram.com/axtbansal/"))
        }

        twitter.setOnClickListener {
            openApp(Uri.parse("https://twitter.com/AxtBansal"))
        }

        linkedIn.setOnClickListener {
            openApp(Uri.parse("https://www.linkedin.com/in/axtbansal/"))
        }

        gmail.setOnClickListener {
            openApp(Uri.parse("mailto:abansal11ae@gmail.com"))
        }

        gitHub.setOnClickListener {
            openApp(Uri.parse("https://github.com/IamBansal"))
        }

    }

    //To open a certain app on click.
    private fun openApp(uri: Uri) {
        val intent =
            Intent(Intent.ACTION_VIEW, uri)
        //            intent.setPackage("com.instagram.android")
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