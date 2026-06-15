package com.project.budgettracker

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.project.budgettracker.data.CategoryLimitManager
import com.project.budgettracker.data.CategoryManager
import com.project.budgettracker.data.ExpenseManager
import com.project.budgettracker.utils.SessionManager

class CategoryLimitActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var categoryManager: CategoryManager
    private lateinit var expenseManager: ExpenseManager
    private lateinit var limitManager: CategoryLimitManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_limit)

        sessionManager = SessionManager(this)
        categoryManager = CategoryManager(this)
        expenseManager = ExpenseManager(this)
        limitManager = CategoryLimitManager(this)

        val spinner = findViewById<Spinner>(R.id.spinnerLimitCategory)
        val edtLimit = findViewById<EditText>(R.id.edtCategoryLimit)
        val btnSave = findViewById<Button>(R.id.btnSaveCategoryLimit)

        val categories = categoryManager.getCategories(sessionManager.getUserId())

        spinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            categories
        )

        btnSave.setOnClickListener {
            if (categories.isEmpty()) {
                Toast.makeText(this, "Create a category first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val limit = edtLimit.text.toString().toDoubleOrNull()

            if (limit == null || limit <= 0) {
                edtLimit.error = "Enter valid limit"
                return@setOnClickListener
            }

            val categoryName = spinner.selectedItem.toString()
            val categoryId = expenseManager.getCategoryId(sessionManager.getUserId(), categoryName)

            val saved = limitManager.saveLimit(
                sessionManager.getUserId(),
                categoryId,
                limit
            )

            Toast.makeText(
                this,
                if (saved) "Category limit saved" else "Could not save limit",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}