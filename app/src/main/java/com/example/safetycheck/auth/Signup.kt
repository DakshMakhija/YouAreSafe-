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
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Matcher
import java.util.regex.Pattern

class Signup : AppCompatActivity() {

    private var signIn: TextView? = null
    private var teller: TextView? = null
    private var signUp: Button? = null
    private var email: TextInputEditText? = null
    private var password: TextInputEditText? = null
    private var confirmPassword: TextInputEditText? = null
    private var firebaseAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        signUp = findViewById(R.id.btnSignUp)
        teller = findViewById(R.id.tvPassTeller)
        signIn = findViewById(R.id.tvAlreadyUser)
        email = findViewById(R.id.etEmailSignup)
        password = findViewById(R.id.etPassSignup)
        confirmPassword = findViewById(R.id.etConfirmPassSignup)
        firebaseAuth = FirebaseAuth.getInstance()

        signUp?.setOnClickListener {
            signUpUser()
        }

        signIn?.setOnClickListener {
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

        password?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                teller?.visibility = View.VISIBLE
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
                        teller?.text = ""
                    }
                    1 -> {
                        teller?.text = "Weak"
                        teller?.setTextColor(resources.getColor(R.color.red))
                    }
                    2 -> {
                        teller?.text = "Moderate"
                        teller?.setTextColor(resources.getColor(R.color.lightRed))
                    }
                    3 -> {
                        teller?.text = "Strong"
                        teller?.setTextColor(resources.getColor(R.color.darkGreen))
                    }
                    4 -> {
                        teller?.text = "Very Strong"
                        teller?.setTextColor(resources.getColor(R.color.green))
                    }
                }

            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun signUpUser() {
        val emailText = email?.text.toString().trim()
        val passwordText = password?.text.toString().trim()
        val confirmPasswordText = confirmPassword?.text.toString().trim()

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
//            if (isValidPassword(passwordText)) {
            val progressBar = ProgressDialog(this)
            progressBar.setMessage("Signing you up..")
            progressBar.show()

            firebaseAuth?.createUserWithEmailAndPassword(emailText, passwordText)
                ?.addOnCompleteListener { task ->
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
//            } else {
//                Toast.makeText(
//                    this,
//                    "Weak password...\nStrong password must contain lowercase, uppercase alphabet," +
//                            " a digit, a special character with no spaces.",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
        }
    }

    private fun isValidPassword(password: String): Boolean {
        val pattern: Pattern
        val specialCharacters = "-@%\\[\\}+'!/#$^?:;,\\(\"\\)~`.*=&\\{>\\]<_"

        /*

        REGEX condition explanation....

        (?=.*[0-9])  This is for that it should have at least a digit.
        (?=.*[a-z])  This is for that it should have at least a lowercase alphabet.
        (?=.*[A-Z])  This is for that it should have at least a uppercase alphabet.
        (?=.*[$specialCharacters])  This is for that it should have at least a special character which are defined above..
        (?=\S+$).{8,20}  This is for that it should have at least 8 and at most 20 characters without any space..

         */

        val passwordRegex =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[$specialCharacters])(?=\\S+$).{8,20}$"
        pattern = Pattern.compile(passwordRegex)
        val matcher: Matcher = pattern.matcher(password)
        return matcher.matches()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, Login::class.java))
    }

}