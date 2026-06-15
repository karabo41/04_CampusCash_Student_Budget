package com.project.budgettracker.data

import android.content.ContentValues
import android.content.Context
import android.util.Log

class CategoryManager(context: Context) {

    private val dbHelper = DatabaseHelper(context)

    companion object {
        private const val TAG = "CategoryManager"
    }

    fun addCategory(userId: Int, categoryName: String): Boolean {
        if (categoryName.isBlank()) return false

        return try {
            val db = dbHelper.writableDatabase

            val values = ContentValues().apply {
                put("user_id", userId)
                put("category_name", categoryName.trim())
            }

            val result = db.insert(DatabaseHelper.TABLE_CATEGORIES, null, values)
            db.close()

            Log.d(TAG, "Category added: $categoryName")
            result != -1L
        } catch (e: Exception) {
            Log.e(TAG, "Failed to add category", e)
            false
        }
    }

    fun getCategories(userId: Int): ArrayList<String> {
        val categories = ArrayList<String>()
        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery(
            "SELECT category_name FROM ${DatabaseHelper.TABLE_CATEGORIES} WHERE user_id = ? ORDER BY category_name ASC",
            arrayOf(userId.toString())
        )

        while (cursor.moveToNext()) {
            categories.add(cursor.getString(0))
        }

        cursor.close()
        db.close()

        return categories
    }
}