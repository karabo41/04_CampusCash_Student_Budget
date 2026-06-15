package com.project.budgettracker.data

import android.content.ContentValues
import android.content.Context
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BadgeManager(context: Context) {

    private val dbHelper = DatabaseHelper(context)

    fun awardBadge(userId: Int, name: String, description: String): Boolean {
        if (hasBadge(userId, name)) return false

        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("user_id", userId)
            put("badge_name", name)
            put("badge_description", description)
            put("earned_date", SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()))
        }

        val result = db.insert(DatabaseHelper.TABLE_BADGES, null, values)
        db.close()
        return result != -1L
    }

    fun hasBadge(userId: Int, name: String): Boolean {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT badge_id FROM ${DatabaseHelper.TABLE_BADGES} WHERE user_id=? AND badge_name=?",
            arrayOf(userId.toString(), name)
        )

        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }

    fun getBadges(userId: Int): ArrayList<String> {
        val badges = ArrayList<String>()
        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery(
            "SELECT badge_name || ' - ' || badge_description FROM ${DatabaseHelper.TABLE_BADGES} WHERE user_id=?",
            arrayOf(userId.toString())
        )

        while (cursor.moveToNext()) badges.add(cursor.getString(0))

        cursor.close()
        db.close()
        return badges
    }
    fun checkAutomaticBadges(
        userId: Int,
        expenseCount: Int
    ) {

        if (expenseCount >= 1) {
            awardBadge(
                userId,
                "First Expense",
                "Added your first expense"
            )
        }

        if (expenseCount >= 10) {
            awardBadge(
                userId,
                "Budget Warrior",
                "Added 10 expenses"
            )
        }
    }
}