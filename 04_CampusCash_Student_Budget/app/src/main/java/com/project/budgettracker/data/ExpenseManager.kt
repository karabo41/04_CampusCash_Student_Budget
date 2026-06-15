package com.project.budgettracker.data

import android.content.ContentValues
import android.content.Context
import android.util.Log

class ExpenseManager(context: Context) {

    private val dbHelper = DatabaseHelper(context)

    companion object {
        private const val TAG = "ExpenseManager"
    }

    fun addExpense(userId: Int, categoryId: Int, amount: Double, date: String, description: String, photoPath: String?): Boolean {
        return try {
            val db = dbHelper.writableDatabase
            val values = ContentValues().apply {
                put("user_id", userId)
                put("category_id", categoryId)
                put("amount", amount)
                put("expense_date", date)
                put("description", description)
                put("photo_path", photoPath)
            }
            val result = db.insert(DatabaseHelper.TABLE_EXPENSES, null, values)
            db.close()
            Log.d(TAG, "Expense added")
            result != -1L
        } catch (e: Exception) {
            Log.e(TAG, "Failed to add expense", e)
            false
        }
    }

    fun getCategoryId(userId: Int, categoryName: String): Int {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT category_id FROM ${DatabaseHelper.TABLE_CATEGORIES} WHERE user_id=? AND category_name=?",
            arrayOf(userId.toString(), categoryName)
        )

        var id = -1
        if (cursor.moveToFirst()) id = cursor.getInt(0)

        cursor.close()
        db.close()
        return id
    }

    fun getExpensesByPeriod(userId: Int, startDate: String, endDate: String): ArrayList<ExpenseItem> {
        val list = ArrayList<ExpenseItem>()
        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT e.expense_id, c.category_name, e.amount, e.expense_date, e.description, e.photo_path
            FROM ${DatabaseHelper.TABLE_EXPENSES} e
            INNER JOIN ${DatabaseHelper.TABLE_CATEGORIES} c
            ON e.category_id = c.category_id
            WHERE e.user_id = ? AND e.expense_date BETWEEN ? AND ?
            ORDER BY e.expense_date DESC
            """.trimIndent(),
            arrayOf(userId.toString(), startDate, endDate)
        )

        while (cursor.moveToNext()) {
            list.add(
                ExpenseItem(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getDouble(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5)
                )
            )
        }

        cursor.close()
        db.close()
        return list
    }

    fun getCategoryTotals(userId: Int, startDate: String, endDate: String): ArrayList<CategoryTotal> {
        val list = ArrayList<CategoryTotal>()
        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT c.category_name, SUM(e.amount)
            FROM ${DatabaseHelper.TABLE_EXPENSES} e
            INNER JOIN ${DatabaseHelper.TABLE_CATEGORIES} c
            ON e.category_id = c.category_id
            WHERE e.user_id = ? AND e.expense_date BETWEEN ? AND ?
            GROUP BY c.category_name
            ORDER BY SUM(e.amount) DESC
            """.trimIndent(),
            arrayOf(userId.toString(), startDate, endDate)
        )

        while (cursor.moveToNext()) {
            list.add(CategoryTotal(cursor.getString(0), cursor.getDouble(1)))
        }

        cursor.close()
        db.close()
        return list
    }

    fun getMonthlyTotal(userId: Int, monthPrefix: String): Double {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT SUM(amount) FROM ${DatabaseHelper.TABLE_EXPENSES} WHERE user_id=? AND expense_date LIKE ?",
            arrayOf(userId.toString(), "$monthPrefix%")
        )

        var total = 0.0
        if (cursor.moveToFirst() && !cursor.isNull(0)) total = cursor.getDouble(0)

        cursor.close()
        db.close()
        return total
    }
}