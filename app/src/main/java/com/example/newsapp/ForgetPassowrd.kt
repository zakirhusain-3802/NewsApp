package com.example.newsapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class ForgetPassowrd : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forget_passowrd)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.forgetPassword)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()
        val resetPasswordButton=findViewById<Button>(R.id.forget_password)

        resetPasswordButton.setOnClickListener {
            val email = findViewById<EditText>(R.id.emailEtF).text.trim().toString()

            if (email.isEmpty()) {
               Toast.makeText(this,"Email flied is empty",Toast.LENGTH_SHORT).show()
            } else {
                resetPassword(email.toString())
            }
        }
    }
    private fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Password reset email sent", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                } else {
                     val exception = task.exception
                    if (exception is FirebaseAuthInvalidUserException) {
                        // User not found exception
                        Toast.makeText(this,"User not exists. Please go back and SignUp",Toast.LENGTH_SHORT).show()
                    } else if (exception is FirebaseAuthInvalidCredentialsException) {
                        // Invalid email exception
                        Toast.makeText(this,"Invalid email",Toast.LENGTH_SHORT).show()

                    } else {
                        // General exception
                        Toast.makeText(this,"Failed to send password reset email.\n" +
                                "Please check internet connectivity",Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, LogInActivity::class.java)
        startActivity(intent)
    }
}