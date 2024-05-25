package com.example.newsapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException

class SignUpActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signup)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        firebaseAuth = FirebaseAuth.getInstance()

        val login = findViewById<TextView>(R.id.loin_in)
        val name = getColoredSpanned("Already Registered,", "#111111")
        val surName = getColoredSpanned("Sign In !", "#DF2E38")
        login.text = Html.fromHtml(name + "<b>" + surName + "</b>")


        login.setOnClickListener {
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
        }
        val nameEditText = findViewById<EditText>(R.id.nameEt)
        val emailEditText = findViewById<EditText>(R.id.emailEt)
        val passwordEditText = findViewById<EditText>(R.id.passET)
        val confromPasswordEditText = findViewById<EditText>(R.id.confirmPassEt)

        val sign_up = findViewById<Button>(R.id.sign_up)

        sign_up.setOnClickListener {
            val userName = nameEditText.text.toString()
            val userEmail = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val conformPassword = confromPasswordEditText.text.toString()

            if (userEmail.isNotEmpty() && password.isNotEmpty() && conformPassword.isNotEmpty() && userName.isNotEmpty()) {
                if (isEmailValid(userEmail.trim().toString())) {
                    // Email is valid, proceed with further actions
                    if (isValidPassword(password.toString())) {

                        if (password == conformPassword) {

                            firebaseAuth.createUserWithEmailAndPassword(userEmail.trim(), password)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        firebaseAuth.currentUser?.sendEmailVerification()
                                            ?.addOnCompleteListener { emailTask ->
                                                if (emailTask.isSuccessful) {
                                                    Toast.makeText(this, "Verification email sent. Please verify before logging in.", Toast.LENGTH_LONG).show()
                                                    firebaseAuth.signOut()
                                                    val intent=Intent(this,LogInActivity::class.java)
                                                    startActivity(intent)

                                                } else {
                                                    Toast.makeText(this, "Failed to send verification email.", Toast.LENGTH_SHORT).show()
                                                }
                                            }

                                    } else {
                                        val exception = it.exception
                                        println(exception.toString())
                                        when (exception) {
                                            is FirebaseAuthUserCollisionException -> {
                                                // Invalid user (user doesn't exist)
                                               Toast.makeText(this,"User Already Exist",Toast.LENGTH_SHORT).show()
                                            }
                                            is FirebaseAuthInvalidCredentialsException -> {
                                                // Invalid credentials (wrong email or password)
                                                Toast.makeText(this,"Invalid credentials",Toast.LENGTH_SHORT).show()

                                            }
                                            else -> {
                                                // Other authentication exceptions
                                                Toast.makeText(
                                                    this,
                                                    "Please check internet connectivity",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    }
                                }

                        } else {
                            Toast.makeText(this,"Password is Not Matching",Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this,"Password: Min 8 chars, 1 lowercase, 1 uppercase, 1 special (!, +, -), 1 digit.",Toast.LENGTH_SHORT).show()
                    }

                } else {
                    // Invalid email, show an error message or handle it appropriately
                    Toast.makeText(this,"Invalid Email Address",Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this,"Please fill all fields",Toast.LENGTH_SHORT).show()
            }


//            val intent=Intent(this,MainActivity::class.java)
//            startActivity(intent)
        }

    }

    private fun getColoredSpanned(text: String, color: String): String? {
        return "<font color=$color>$text</font>"
    }

    fun isEmailValid(email: String): Boolean {
        val emailRegex = Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        return emailRegex.matches(email)
    }

     fun isValidPassword(password: String): Boolean {
        if (password.length < 8) return false
        if (password.filter { it.isDigit() }.firstOrNull() == null) return false
        if (password.filter { it.isLetter() }.filter { it.isUpperCase() }
                .firstOrNull() == null) return false
        if (password.filter { it.isLetter() }.filter { it.isLowerCase() }
                .firstOrNull() == null) return false
        if (password.filter { !it.isLetterOrDigit() }.firstOrNull() == null) return false

        return true
    }

}