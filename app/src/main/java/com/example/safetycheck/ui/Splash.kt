package com.example.safetycheck.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.safetycheck.R
import com.example.safetycheck.auth.Login
import com.example.safetycheck.databinding.ActivitySplashScreenBinding

class Splash : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var animation: Animation
    private lateinit var fadeAnimation: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        animation =  AnimationUtils.loadAnimation(this, R.anim.logo_anim)
        fadeAnimation =  AnimationUtils.loadAnimation(this, R.anim.fade)

        binding.ivSplash.animation = animation
        binding.ivSmile.animation = fadeAnimation
        binding.tvWeAreHere.animation = fadeAnimation

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, Login::class.java))
        }, 2500)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

}