package com.example.safetycheck.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.ContactsContract
import android.telephony.SmsManager
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.safetycheck.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AddContact : AppCompatActivity() {

    private lateinit var name: EditText
    private lateinit var relation: EditText
    private lateinit var number: EditText
    private lateinit var otpEnter: EditText
    private lateinit var addContact: Button
    private lateinit var addContactVia: Button
    private lateinit var text: TextView
    private lateinit var firebaseAuth: FirebaseAuth

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)

        name = findViewById(R.id.etName)
        relation = findViewById(R.id.etRelation)
        number = findViewById(R.id.etNumber)
        otpEnter = findViewById(R.id.etOtp)
        addContact = findViewById(R.id.btnAdd)
        addContactVia = findViewById(R.id.addVia)
        text = findViewById(R.id.tvAuto)
        firebaseAuth = FirebaseAuth.getInstance()

        addContact.setOnClickListener {
            addContactNumber()
        }

        addContactVia.setOnClickListener {
            getContact()
        }

        addContact.tag = "send"
        addContact.text = "Send OTP"

        otpEnter.visibility = View.GONE
        text.visibility = View.GONE

    }

    //To get the location
    private fun getContact() {
        if (checkPermissions()) {
            //To check Permissions
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_CONTACTS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            val uri = Uri.parse("content://contacts")
            val intent = Intent(Intent.ACTION_PICK, uri)
            intent.setDataAndType(uri, ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE)
            startActivityForResult(intent, 1)
        } else {
            requestPermission()
        }
    }

    //To request the permissions.
    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.READ_CONTACTS
            ),
            100
        )
    }

    //To show that permissions are checked.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Granted", Toast.LENGTH_SHORT).show()
                getContact()
            } else {
                Toast.makeText(this, "Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //To check if permissions are granted or not.
    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            1 -> {
                if (resultCode == Activity.RESULT_OK) {

                    val contactData: Uri? = data?.data

                    val projection = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)

                    val cursor = contentResolver.query(
                        contactData!!, projection,
                        null, null, null
                    )
                    cursor!!.moveToFirst()

                    val numberColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    val numberC = cursor.getString(numberColumnIndex)

                    val nameColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                    val nameC = cursor.getString(nameColumnIndex)
                    name.text = Editable.Factory.getInstance().newEditable(nameC)

                    var newNumber = ""
                    if (numberC.length > 10) {
                            for (i in numberC.reversed()) {
                                if (i != ' ' && newNumber.length < 10) {
                                    newNumber += i
                                }
                            }
                    }

                    number.text = Editable.Factory.getInstance().newEditable(newNumber.reversed())
                    cursor.close()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun addContactNumber() {

        val nameText = name.text.toString().trim()
        val relationText = relation.text.toString().trim()
        val numberText = number.text.toString().trim()
        if (TextUtils.isEmpty(nameText) || TextUtils.isEmpty(relationText) || TextUtils.isEmpty(
                numberText
            ) || numberText.length != 10
        ) {
            val alert = AlertDialog.Builder(this)
            alert.setTitle("Adding new contact failed!!")
                .setMessage("Fill all credentials first or check your number again.")
                .setPositiveButton("Okay") { _, _ -> }
                .create()
                .show()
        } else {
            otpEnter.visibility = View.VISIBLE
            text.visibility = View.VISIBLE

            val otp = (1000..9999).random().toString()
            Toast.makeText(this, "OTP is : $otp", Toast.LENGTH_SHORT).show()
            val obj = SmsManager.getDefault()
            obj.sendTextMessage(
                numberText,
                null,
                "Hey!\nI have added your number as emergency contact in 'You are Safe!!' app.\nPlease verify the number by otp.\nOTP is $otp .\nOTP will expire in 20 seconds.",
                null,
                null
            )

            otpEnter.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0?.length == 4) {
                        val progressBar = ProgressDialog(this@AddContact)
                        progressBar.setMessage("Auto-verifying OTP..")
                        progressBar.show()

                        Handler(Looper.getMainLooper()).postDelayed(
                            {
                                progressBar.dismiss()
                                if (p0.contentEquals(otp)) {
                                    val map = HashMap<String, Any>()
                                    map["name"] = nameText
                                    map["relation"] = relationText
                                    map["number"] = numberText

                                    FirebaseDatabase.getInstance().reference.child("UsersSafety")
                                        .child(firebaseAuth.currentUser!!.uid).child(numberText)
                                        .updateChildren(map)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                Toast.makeText(
                                                    this@AddContact,
                                                    "Number added successfully.",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                addContact.tag = "add"
                                                addContact.text = "Add contact"
                                                startActivity(
                                                    Intent(
                                                        this@AddContact,
                                                        MainActivity::class.java
                                                    )
                                                )
                                            } else {
                                                Toast.makeText(
                                                    this@AddContact,
                                                    task.exception?.message,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }

                                } else {
                                    Toast.makeText(
                                        this@AddContact,
                                        "OTP not verified.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }, 2000
                        )
                    }
                }

                override fun afterTextChanged(p0: Editable?) {}
            })

        }
    }
}