package com.example.safetycheck.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.example.safetycheck.R
import com.example.safetycheck.auth.Login

class Splash : AppCompatActivity() {

    private lateinit var logo : ImageView
    private lateinit var head : TextView
    private lateinit var smile : ImageView
    private lateinit var text : TextView
    private lateinit var animation: Animation
    private lateinit var fadeAnimation: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        logo = findViewById(R.id.ivSplash)
        head = findViewById(R.id.tvSplash)
        smile = findViewById(R.id.tv3Splash)
        text = findViewById(R.id.tv2Splash)
        animation =  AnimationUtils.loadAnimation(this, R.anim.logo_anim)
        fadeAnimation =  AnimationUtils.loadAnimation(this, R.anim.fade)

        logo.animation = animation
//        head.animation = animation
        smile.animation = fadeAnimation
        text.animation = fadeAnimation

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, Login::class.java))
        }, 2500)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

}