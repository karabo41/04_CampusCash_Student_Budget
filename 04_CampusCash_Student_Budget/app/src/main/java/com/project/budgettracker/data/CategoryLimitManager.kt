package com.project.budgettracker.data

import android.content.ContentValues
import android.content.Context
import android.util.Log

class CategoryLimitManager(context: Context) {

    private val dbHelper = DatabaseHelper(context)

    fun saveLimit(userId: Int, categoryId: Int, limitAmount: Double): Boolean {
        return try {
            val db = dbHelper.writableDatabase

            db.delete(
                DatabaseHelper.TABLE_CATEGORY_LIMITS,
                "user_id=? AND category_id=?",
                arrayOf(userId.toString(), categoryId.toString())
            )

            val values = ContentValues().apply {
                put("user_id", userId)
                put("category_id", categoryId)
                put("limit_amount", limitAmount)
            }

            val result = db.insert(DatabaseHelper.TABLE_CATEGORY_LIMITS, null, values)
            db.close()

            result != -1L
        } catch (e: Exception) {
            Log.e("CategoryLimitManager", "Could not save category limit", e)
            false
        }
    }
}