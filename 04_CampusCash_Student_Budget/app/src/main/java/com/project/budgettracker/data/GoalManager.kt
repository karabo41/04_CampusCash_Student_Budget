package com.project.budgettracker.data

import android.content.ContentValues
import android.content.Context
import android.util.Log

class GoalManager(context: Context) {

    private val dbHelper = DatabaseHelper(context)

    fun saveMonthlyGoal(userId: Int, monthYear: String, minGoal: Double, maxGoal: Double): Boolean {
        return try {
            val db = dbHelper.writableDatabase
            db.delete(DatabaseHelper.TABLE_GOALS, "user_id=? AND month_year=?", arrayOf(userId.toString(), monthYear))

            val values = ContentValues().apply {
                put("user_id", userId)
                put("month_year", monthYear)
                put("min_goal", minGoal)
                put("max_goal", maxGoal)
            }

            val result = db.insert(DatabaseHelper.TABLE_GOALS, null, values)
            db.close()
            result != -1L
        } catch (e: Exception) {
            Log.e("GoalManager", "Goal save failed", e)
            false
        }
    }

    fun getMonthlyGoal(userId: Int, monthYear: String): Pair<Double, Double>? {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT min_goal, max_goal FROM ${DatabaseHelper.TABLE_GOALS} WHERE user_id=? AND month_year=?",
            arrayOf(userId.toString(), monthYear)
        )

        var goal: Pair<Double, Double>? = null
        if (cursor.moveToFirst()) goal = Pair(cursor.getDouble(0), cursor.getDouble(1))

        cursor.close()
        db.close()
        return goal
    }
}