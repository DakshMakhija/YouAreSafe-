package com.example.safetycheck.auth

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.safetycheck.R
import com.example.safetycheck.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Matcher
import java.util.regex.Pattern

class Signup : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnSignUp.setOnClickListener {
            signUpUser()
        }

        binding.tvAlreadyUser.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }

        showPassStrength()

    }

    private fun showPassStrength() {

        var count: Int

        val patternNumbers: Pattern
        val patternAlpha: Pattern
        val patternCapAlpha: Pattern
        val patternSpecial: Pattern
        val specialCharacters = "-@%\\[\\}+'!/#$^?:;,\\(\"\\)~`.*=&\\{>\\]<_"

        val passwordRegexNumbers = "^(?=.*[0-9])"
        val alphabetRegex = "(?=.*[a-z])"
        val capitalAlphabetRegex = "(?=.*[A-Z])"
        val specialRegex = "^(?=.*[$specialCharacters])(?=\\S+\$)"

        patternNumbers = Pattern.compile(passwordRegexNumbers)
        patternAlpha = Pattern.compile(alphabetRegex)
        patternCapAlpha = Pattern.compile(capitalAlphabetRegex)
        patternSpecial = Pattern.compile(specialRegex)

        binding.etPassSignup.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.tvPassTeller.visibility = View.VISIBLE
                count = 0
                if (patternNumbers.matcher(p0!!).find()) {
                    count++
                }
                if (patternAlpha.matcher(p0.toString().trim()).find()) {
                    count++
                }
                if (patternCapAlpha.matcher(p0.toString().trim()).find()) {
                    count++
                }
                if (patternSpecial.matcher(p0.toString().trim()).find()) {
                    count++
                }

                when (count) {
                    0 -> {
                        binding.tvPassTeller.text = ""
                    }
                    1 -> {
                        binding.tvPassTeller.text = "Weak"
                        binding.tvPassTeller.setTextColor(resources.getColor(R.color.red))
                    }
                    2 -> {
                        binding.tvPassTeller.text = "Moderate"
                        binding.tvPassTeller.setTextColor(resources.getColor(R.color.lightRed))
                    }
                    3 -> {
                        binding.tvPassTeller.text = "Strong"
                        binding.tvPassTeller.setTextColor(resources.getColor(R.color.darkGreen))
                    }
                    4 -> {
                        binding.tvPassTeller.text = "Very Strong"
                        binding.tvPassTeller.setTextColor(resources.getColor(R.color.green))
                    }
                }

            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun signUpUser() {
        val emailText = binding.etEmailSignup.text.toString().trim()
        val passwordText = binding.etPassSignup.text.toString().trim()
        val confirmPasswordText = binding.etConfirmPassSignup.text.toString().trim()

        if (TextUtils.isEmpty(emailText) || TextUtils.isEmpty(passwordText) || TextUtils.isEmpty(
                confirmPasswordText
            )
        ) {
            val alert = AlertDialog.Builder(this)
            alert.setTitle("Signup failed!!")
                .setMessage("Fill all credentials first.")
                .setPositiveButton("Okay") { _, _ -> }
                .create()
                .show()
        } else if (passwordText != confirmPasswordText) {
            val alert = AlertDialog.Builder(this)
            alert.setTitle("Signup failed!!")
                .setMessage("Password didn't matched.")
                .setPositiveButton("Okay") { _, _ -> }
                .create()
                .show()
        } else {
            val progressBar = ProgressDialog(this)
            progressBar.setMessage("Signing you up..")
            progressBar.show()

            firebaseAuth.createUserWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener { task ->
                    progressBar.dismiss()
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this,
                            "Signed up successfully.",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(this, Login::class.java))
                    } else {
                        Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, Login::class.java))
    }

}