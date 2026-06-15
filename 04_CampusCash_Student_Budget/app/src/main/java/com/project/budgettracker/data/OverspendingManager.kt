package com.project.budgettracker.data

import android.content.Context

class OverspendingManager(context: Context) {

    private val dbHelper = DatabaseHelper(context)

    fun getOverspendingCategories(userId: Int, startDate: String, endDate: String): ArrayList<String> {
        val list = ArrayList<String>()
        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT c.category_name, SUM(e.amount), cl.limit_amount
            FROM expenses e
            INNER JOIN categories c ON e.category_id = c.category_id
            INNER JOIN category_limits cl ON c.category_id = cl.category_id
            WHERE e.user_id = ?
            AND e.expense_date BETWEEN ? AND ?
            GROUP BY c.category_name, cl.limit_amount
            """.trimIndent(),
            arrayOf(userId.toString(), startDate, endDate)
        )

        while (cursor.moveToNext()) {
            val category = cursor.getString(0)
            val spent = cursor.getDouble(1)
            val limit = cursor.getDouble(2)

            if (spent > limit) {
                list.add("$category exceeded by R${String.format("%.2f", spent - limit)}")
            }
        }

        cursor.close()
        db.close()
        return list
    }
}