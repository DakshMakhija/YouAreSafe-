package com.example.safetycheck.auth

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.safetycheck.databinding.ActivityLoginBinding
import com.example.safetycheck.main.MainActivity
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnSignIn.setOnClickListener {
            signInUser()
        }

        binding.tvForgotPass.setOnClickListener {
            Toast.makeText(this, "Not implemented yet!!", Toast.LENGTH_SHORT).show()
        }

        binding.tvNewUser.setOnClickListener {
            startActivity(Intent(this, Signup::class.java))
        }

    }

    private fun signInUser() {

        val emailText = binding.etEmailLogin.text.toString().trim()
        val passText = binding.etPassLogin.text.toString().trim()

        if (TextUtils.isEmpty(emailText) || TextUtils.isEmpty(passText)) {
            val alert = AlertDialog.Builder(this)
            alert.setTitle("Login failed!!")
                .setMessage("Fill all credentials first.")
                .setPositiveButton("Okay"){_,_-> }
                .create()
                .show()
        } else {
            val progressBar = ProgressDialog(this)
            progressBar.setMessage("Logging in..")
            progressBar.show()

            firebaseAuth.signInWithEmailAndPassword(emailText, passText).addOnCompleteListener { task ->
                progressBar.dismiss()
                if(task.isSuccessful) {
                    Toast.makeText(this, "Logged in successfully.", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

}