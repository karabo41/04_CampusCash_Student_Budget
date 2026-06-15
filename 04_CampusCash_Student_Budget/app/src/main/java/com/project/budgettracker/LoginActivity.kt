package com.project.budgettracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.project.budgettracker.data.UserManager
import com.project.budgettracker.utils.SessionManager

class LoginActivity : AppCompatActivity() {

    private lateinit var userManager: UserManager
    private lateinit var sessionManager: SessionManager

    companion object {
        private const val TAG = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        userManager = UserManager(this)
        sessionManager = SessionManager(this)

        if (sessionManager.isLoggedIn()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        val edtUsername = findViewById<EditText>(R.id.edtLoginUsername)
        val edtPassword = findViewById<EditText>(R.id.edtLoginPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnGoToRegister = findViewById<Button>(R.id.btnGoToRegister)

        btnLogin.setOnClickListener {
            val username = edtUsername.text.toString().trim()
            val password = edtPassword.text.toString().trim()

            if (username.isBlank()) {
                edtUsername.error = "Enter username"
                return@setOnClickListener
            }

            if (password.isBlank()) {
                edtPassword.error = "Enter password"
                return@setOnClickListener
            }

            val userId = userManager.loginUser(username, password)

            if (userId != -1) {
                sessionManager.saveUserSession(userId, username)
                Log.d(TAG, "Login successful for user ID: $userId")

                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
            }
        }

        btnGoToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}