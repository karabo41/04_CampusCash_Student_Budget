package com.project.budgettracker.data

import android.content.ContentValues
import android.content.Context

class ReminderManager(context: Context) {

    private val dbHelper = DatabaseHelper(context)

    fun addReminder(userId: Int, title: String, dueDate: String, note: String): Boolean {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put("user_id", userId)
            put("title", title)
            put("due_date", dueDate)
            put("note", note)
        }

        val result = db.insert(DatabaseHelper.TABLE_REMINDERS, null, values)
        db.close()

        return result != -1L
    }

    fun getReminders(userId: Int): ArrayList<String> {
        val reminders = ArrayList<String>()
        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery(
            "SELECT title, due_date, note FROM ${DatabaseHelper.TABLE_REMINDERS} WHERE user_id=? ORDER BY due_date ASC",
            arrayOf(userId.toString())
        )

        while (cursor.moveToNext()) {
            reminders.add("${cursor.getString(1)} | ${cursor.getString(0)}\n${cursor.getString(2)}")
        }

        cursor.close()
        db.close()

        return reminders
    }
}