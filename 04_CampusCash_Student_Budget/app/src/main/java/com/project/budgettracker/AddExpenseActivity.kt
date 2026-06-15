package com.project.budgettracker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.project.budgettracker.data.BadgeManager
import com.project.budgettracker.data.CategoryManager
import com.project.budgettracker.data.ExpenseManager
import com.project.budgettracker.utils.SessionManager

class AddExpenseActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var categoryManager: CategoryManager
    private lateinit var expenseManager: ExpenseManager
    private lateinit var badgeManager: BadgeManager

    private val categories = ArrayList<String>()
    private var selectedPhotoUri: Uri? = null

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            selectedPhotoUri = uri
            findViewById<TextView>(R.id.txtPhotoStatus).text =
                if (uri != null) "Receipt photo selected" else "No photo selected"
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        sessionManager = SessionManager(this)
        categoryManager = CategoryManager(this)
        expenseManager = ExpenseManager(this)
        badgeManager = BadgeManager(this)

        val edtAmount = findViewById<EditText>(R.id.edtAmount)
        val edtDate = findViewById<EditText>(R.id.edtDate)
        val edtDescription = findViewById<EditText>(R.id.edtDescription)
        val spinnerCategory = findViewById<Spinner>(R.id.spinnerCategory)
        val btnChoosePhoto = findViewById<Button>(R.id.btnChoosePhoto)
        val btnSaveExpense = findViewById<Button>(R.id.btnSaveExpense)

        categories.clear()
        categories.addAll(categoryManager.getCategories(sessionManager.getUserId()))

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            categories
        )
        spinnerCategory.adapter = adapter

        btnChoosePhoto.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        btnSaveExpense.setOnClickListener {
            val amountText = edtAmount.text.toString().trim()
            val date = edtDate.text.toString().trim()
            val description = edtDescription.text.toString().trim()

            if (categories.isEmpty()) {
                Toast.makeText(this, "Create a category first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val amount = amountText.toDoubleOrNull()
            if (amount == null || amount <= 0) {
                edtAmount.error = "Enter valid amount"
                return@setOnClickListener
            }

            if (!date.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))) {
                edtDate.error = "Use YYYY-MM-DD format"
                return@setOnClickListener
            }

            if (description.length < 3) {
                edtDescription.error = "Enter description"
                return@setOnClickListener
            }

            val selectedCategory = spinnerCategory.selectedItem.toString()
            val categoryId = expenseManager.getCategoryId(
                sessionManager.getUserId(),
                selectedCategory
            )

            val saved = expenseManager.addExpense(
                sessionManager.getUserId(),
                categoryId,
                amount,
                date,
                description,
                selectedPhotoUri?.toString()
            )

            if (saved) {
                badgeManager.awardBadge(
                    sessionManager.getUserId(),
                    "First Expense",
                    "Added your first tracked expense"
                )

                Toast.makeText(this, "Expense saved", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Could not save expense", Toast.LENGTH_SHORT).show()
            }
        }
    }
}