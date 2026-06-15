package com.project.budgettracker

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.project.budgettracker.data.CategoryManager
import com.project.budgettracker.utils.SessionManager

class CategoryActivity : AppCompatActivity() {

    private lateinit var categoryManager: CategoryManager
    private lateinit var sessionManager: SessionManager
    private lateinit var adapter: ArrayAdapter<String>
    private val categoryList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        categoryManager = CategoryManager(this)
        sessionManager = SessionManager(this)

        val edtCategoryName = findViewById<EditText>(R.id.edtCategoryName)
        val btnSaveCategory = findViewById<Button>(R.id.btnSaveCategory)
        val listCategories = findViewById<ListView>(R.id.listCategories)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, categoryList)
        listCategories.adapter = adapter

        loadCategories()

        btnSaveCategory.setOnClickListener {
            val name = edtCategoryName.text.toString().trim()

            if (name.length < 2) {
                edtCategoryName.error = "Enter a valid category"
                return@setOnClickListener
            }

            val saved = categoryManager.addCategory(sessionManager.getUserId(), name)

            if (saved) {
                Toast.makeText(this, "Category saved", Toast.LENGTH_SHORT).show()
                edtCategoryName.text.clear()
                loadCategories()
            } else {
                Toast.makeText(this, "Could not save category", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadCategories() {
        categoryList.clear()
        categoryList.addAll(categoryManager.getCategories(sessionManager.getUserId()))
        adapter.notifyDataSetChanged()
    }
}
