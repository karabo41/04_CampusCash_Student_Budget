package com.project.budgettracker.data

data class ExpenseItem(
    val expenseId: Int,
    val categoryName: String,
    val amount: Double,
    val date: String,
    val description: String,
    val photoPath: String?
)