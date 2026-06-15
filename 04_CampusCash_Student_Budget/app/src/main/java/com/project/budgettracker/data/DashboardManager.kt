package com.project.budgettracker.data

import android.content.Context

class DashboardManager(context: Context) {

    private val expenseManager = ExpenseManager(context)
    private val goalManager = GoalManager(context)

    fun getGoalProgress(userId: Int, monthYear: String): Int {

        val goal = goalManager.getMonthlyGoal(userId, monthYear)
            ?: return 0

        val maxGoal = goal.second

        if (maxGoal <= 0) return 0

        val totalSpent = expenseManager.getMonthlyTotal(userId, monthYear)

        return ((totalSpent / maxGoal) * 100).toInt()
    }
}