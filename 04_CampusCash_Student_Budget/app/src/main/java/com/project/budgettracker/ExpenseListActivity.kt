package com.project.budgettracker

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.project.budgettracker.data.ExpenseManager
import com.project.budgettracker.utils.SessionManager

class ExpenseListActivity : AppCompatActivity() {

    private lateinit var expenseManager: ExpenseManager
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_list)

        expenseManager = ExpenseManager(this)
        sessionManager = SessionManager(this)

        val edtStartDate = findViewById<EditText>(R.id.edtExpenseStartDate)
        val edtEndDate = findViewById<EditText>(R.id.edtExpenseEndDate)
        val btnLoad = findViewById<Button>(R.id.btnLoadExpenses)
        val listExpenses = findViewById<ListView>(R.id.listExpenses)

        btnLoad.setOnClickListener {
            val start = edtStartDate.text.toString().trim()
            val end = edtEndDate.text.toString().trim()

            if (start.isBlank() || end.isBlank()) {
                Toast.makeText(this, "Enter start and end dates", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val expenses = expenseManager.getExpensesByPeriod(
                sessionManager.getUserId(),
                start,
                end
            )

            val displayList = expenses.map {
                "${it.date} | ${it.categoryName}\nR${it.amount} - ${it.description}"
            }

            listExpenses.adapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                displayList
            )

            listExpenses.setOnItemClickListener { _, _, position, _ ->
                val photoPath = expenses[position].photoPath

                if (!photoPath.isNullOrBlank()) {
                    val intent = Intent(this, PhotoViewerActivity::class.java)
                    intent.putExtra("PHOTO_PATH", photoPath)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "No photo attached", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}