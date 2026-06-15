package com.project.budgettracker.utils

import android.content.Context

class SessionManager(context: Context) {

    private val sharedPreferences = context.getSharedPreferences(
        "SmartBudgetSession",
        Context.MODE_PRIVATE
    )

    fun saveUserSession(userId: Int, username: String) {
        sharedPreferences.edit()
            .putInt("USER_ID", userId)
            .putString("USERNAME", username)
            .putBoolean("IS_LOGGED_IN", true)
            .apply()
    }

    fun getUserId(): Int {
        return sharedPreferences.getInt("USER_ID", -1)
    }

    fun getUsername(): String {
        return sharedPreferences.getString("USERNAME", "") ?: ""
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("IS_LOGGED_IN", false)
    }

    fun logout() {
        sharedPreferences.edit().clear().apply()
    }
}