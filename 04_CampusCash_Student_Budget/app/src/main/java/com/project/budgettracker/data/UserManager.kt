package com.project.budgettracker.data

import android.content.ContentValues
import android.content.Context
import android.util.Log

class UserManager(context: Context) {

    private val dbHelper = DatabaseHelper(context)

    companion object {
        private const val TAG = "UserManager"
    }

    fun registerUser(username: String, password: String): Boolean {

        if (username.isBlank() || password.isBlank()) {
            return false
        }

        if (usernameExists(username)) {
            return false
        }

        return try {

            val db = dbHelper.writableDatabase

            val values = ContentValues().apply {
                put("username", username)
                put("password", password)
            }

            val result = db.insert(
                DatabaseHelper.TABLE_USERS,
                null,
                values
            )

            db.close()

            Log.d(TAG, "User registered")

            result != -1L

        } catch (e: Exception) {

            Log.e(TAG, "Registration failed", e)

            false
        }
    }

    fun usernameExists(username: String): Boolean {

        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM users WHERE username = ?",
            arrayOf(username)
        )

        val exists = cursor.count > 0

        cursor.close()
        db.close()

        return exists
    }

    fun loginUser(username: String, password: String): Int {

        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT user_id
            FROM users
            WHERE username = ?
            AND password = ?
            """.trimIndent(),
            arrayOf(username, password)
        )

        var userId = -1

        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0)
        }

        cursor.close()
        db.close()

        return userId
    }
}