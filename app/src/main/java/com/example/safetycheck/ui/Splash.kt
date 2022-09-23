package com.example.safetycheck.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.safetycheck.R
import com.example.safetycheck.auth.Login

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            startActivity(Intent(this, Login::class.java))
        }, 2500)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

}