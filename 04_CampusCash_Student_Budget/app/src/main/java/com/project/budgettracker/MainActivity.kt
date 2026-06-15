package com.project.budgettracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.project.budgettracker.data.BadgeManager
import com.project.budgettracker.data.ExpenseManager
import com.project.budgettracker.data.GoalManager
import com.project.budgettracker.data.OverspendingManager
import com.project.budgettracker.utils.SessionManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var expenseManager: ExpenseManager
    private lateinit var goalManager: GoalManager
    private lateinit var badgeManager: BadgeManager
    private lateinit var overspendingManager: OverspendingManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionManager = SessionManager(this)
        expenseManager = ExpenseManager(this)
        goalManager = GoalManager(this)
        badgeManager = BadgeManager(this)
        overspendingManager = OverspendingManager(this)

        if (!sessionManager.isLoggedIn()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.txtWelcome).text =
            "Welcome, ${sessionManager.getUsername()}"

        findViewById<Button>(R.id.btnAddExpense).setOnClickListener {
            startActivity(Intent(this, AddExpenseActivity::class.java))
        }

        findViewById<Button>(R.id.btnCategories).setOnClickListener {
            startActivity(Intent(this, CategoryActivity::class.java))
        }

        findViewById<Button>(R.id.btnGoals).setOnClickListener {
            startActivity(Intent(this, GoalsActivity::class.java))
        }

        findViewById<Button>(R.id.btnReports).setOnClickListener {
            startActivity(Intent(this, ReportsActivity::class.java))
        }

        findViewById<Button>(R.id.btnGraph).setOnClickListener {
            startActivity(Intent(this, GraphActivity::class.java))
        }

        findViewById<Button>(R.id.btnBadges).setOnClickListener {
            startActivity(Intent(this, BadgesActivity::class.java))
        }

        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            sessionManager.logout()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        updateDashboard()
    }

    private fun updateDashboard() {
        val userId = sessionManager.getUserId()
        val month = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(Date())
        val startDate = "$month-01"
        val endDate = "$month-31"

        val totalSpent = expenseManager.getMonthlyTotal(userId, month)

        findViewById<TextView>(R.id.txtMonthlySpending).text =
            "R${String.format("%.2f", totalSpent)} spent this month"

        val goal = goalManager.getMonthlyGoal(userId, month)
        val progressBar = findViewById<ProgressBar>(R.id.progressGoal)
        val txtGoalStatus = findViewById<TextView>(R.id.txtGoalStatus)

        if (goal != null) {
            val minGoal = goal.first
            val maxGoal = goal.second
            val progress = if (maxGoal > 0) ((totalSpent / maxGoal) * 100).toInt() else 0

            progressBar.progress = progress.coerceIn(0, 100)

            txtGoalStatus.text =
                "Goal: R${String.format("%.2f", minGoal)} - R${String.format("%.2f", maxGoal)} | Progress: ${progress.coerceAtMost(100)}%"

            if (totalSpent <= maxGoal && totalSpent >= minGoal) {
                badgeManager.awardBadge(userId, "Goal Keeper", "Stayed within monthly budget goals")
            }
        } else {
            progressBar.progress = 0
            txtGoalStatus.text = "No monthly goal set for $month"
        }

        val overspending = overspendingManager.getOverspendingCategories(userId, startDate, endDate)
        val txtOverspending = findViewById<TextView>(R.id.txtOverspending)

        txtOverspending.text = if (overspending.isEmpty()) {
            "No overspending detected"
        } else {
            "⚠ Overspending:\n${overspending.joinToString("\n")}"
        }

        val badgeCount = badgeManager.getBadges(userId).size
        findViewById<TextView>(R.id.txtBadgeSummary).text = "Badges earned: $badgeCount"
    }
}