package com.project.budgettracker

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.project.budgettracker.data.ExpenseManager
import com.project.budgettracker.utils.SessionManager

class ReportsActivity : AppCompatActivity() {

    private lateinit var expenseManager: ExpenseManager
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reports)

        expenseManager = ExpenseManager(this)
        sessionManager = SessionManager(this)

        val edtStartDate = findViewById<EditText>(R.id.edtStartDate)
        val edtEndDate = findViewById<EditText>(R.id.edtEndDate)
        val btnGenerate = findViewById<Button>(R.id.btnGenerateReport)
        val listView = findViewById<ListView>(R.id.listReport)

        btnGenerate.setOnClickListener {
            val totals = expenseManager.getCategoryTotals(
                sessionManager.getUserId(),
                edtStartDate.text.toString().trim(),
                edtEndDate.text.toString().trim()
            )

            val reportList = ArrayList<String>()

            totals.forEach {
                reportList.add("${it.categoryName}: R${String.format("%.2f", it.totalAmount)}")
            }

            if (reportList.isEmpty()) {
                reportList.add("No category spending found for this period.")
            }

            listView.adapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                reportList
            )
        }
    }
}