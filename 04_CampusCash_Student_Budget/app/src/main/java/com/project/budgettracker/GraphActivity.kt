package com.project.budgettracker

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.project.budgettracker.data.ExpenseManager
import com.project.budgettracker.utils.SessionManager
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

class GraphActivity : AppCompatActivity() {

    private lateinit var expenseManager: ExpenseManager
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)

        expenseManager = ExpenseManager(this)
        sessionManager = SessionManager(this)

        val edtStartDate = findViewById<EditText>(R.id.edtGraphStartDate)
        val edtEndDate = findViewById<EditText>(R.id.edtGraphEndDate)
        val btnLoadGraph = findViewById<Button>(R.id.btnLoadGraph)
        val barChart = findViewById<BarChart>(R.id.barChart)

        btnLoadGraph.setOnClickListener {
            val start = edtStartDate.text.toString().trim()
            val end = edtEndDate.text.toString().trim()

            if (start.isBlank() || end.isBlank()) {
                Toast.makeText(this, "Enter date range", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val totals = expenseManager.getCategoryTotals(
                sessionManager.getUserId(),
                start,
                end
            )

            if (totals.isEmpty()) {
                Toast.makeText(this, "No spending found", Toast.LENGTH_SHORT).show()
                barChart.clear()
                return@setOnClickListener
            }

            val entries = ArrayList<BarEntry>()

            totals.forEachIndexed { index, item ->
                entries.add(BarEntry(index.toFloat(), item.totalAmount.toFloat()))
            }

            val dataSet = BarDataSet(entries, "Category Spending")
            val data = BarData(dataSet)

            barChart.data = data
            barChart.description.text = "Spending by category"
            barChart.invalidate()
        }
    }
}