package com.project.budgettracker

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.project.budgettracker.data.GoalManager
import com.project.budgettracker.utils.SessionManager

class GoalsActivity : AppCompatActivity() {

    private lateinit var goalManager: GoalManager
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goals)

        goalManager = GoalManager(this)
        sessionManager = SessionManager(this)

        val edtMonth = findViewById<EditText>(R.id.edtMonth)
        val edtMinGoal = findViewById<EditText>(R.id.edtMinGoal)
        val edtMaxGoal = findViewById<EditText>(R.id.edtMaxGoal)
        val btnSaveGoal = findViewById<Button>(R.id.btnSaveGoal)

        btnSaveGoal.setOnClickListener {

            val month = edtMonth.text.toString().trim()
            val minGoal = edtMinGoal.text.toString().toDoubleOrNull()
            val maxGoal = edtMaxGoal.text.toString().toDoubleOrNull()

            if (month.isBlank() || minGoal == null || maxGoal == null) {
                Toast.makeText(this, "Complete all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val saved = goalManager.saveMonthlyGoal(
                sessionManager.getUserId(),
                month,
                minGoal,
                maxGoal
            )

            Toast.makeText(
                this,
                if (saved) "Goal saved" else "Failed to save goal",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}