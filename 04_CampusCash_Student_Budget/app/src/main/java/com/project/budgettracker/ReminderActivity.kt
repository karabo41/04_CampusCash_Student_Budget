package com.project.budgettracker

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.project.budgettracker.data.ReminderManager
import com.project.budgettracker.utils.SessionManager

class ReminderActivity : AppCompatActivity() {

    private lateinit var reminderManager: ReminderManager
    private lateinit var sessionManager: SessionManager
    private val reminderList = ArrayList<String>()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)

        reminderManager = ReminderManager(this)
        sessionManager = SessionManager(this)

        val edtTitle = findViewById<EditText>(R.id.edtReminderTitle)
        val edtDate = findViewById<EditText>(R.id.edtReminderDate)
        val edtNote = findViewById<EditText>(R.id.edtReminderNote)
        val btnSave = findViewById<Button>(R.id.btnSaveReminder)
        val listView = findViewById<ListView>(R.id.listReminders)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, reminderList)
        listView.adapter = adapter

        loadReminders()

        btnSave.setOnClickListener {
            val title = edtTitle.text.toString().trim()
            val date = edtDate.text.toString().trim()
            val note = edtNote.text.toString().trim()

            if (title.isBlank() || date.isBlank()) {
                Toast.makeText(this, "Enter title and due date", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val saved = reminderManager.addReminder(
                sessionManager.getUserId(),
                title,
                date,
                note
            )

            if (saved) {
                Toast.makeText(this, "Reminder saved", Toast.LENGTH_SHORT).show()
                edtTitle.text.clear()
                edtDate.text.clear()
                edtNote.text.clear()
                loadReminders()
            }
        }
    }

    private fun loadReminders() {
        reminderList.clear()
        reminderList.addAll(reminderManager.getReminders(sessionManager.getUserId()))
        adapter.notifyDataSetChanged()
    }
}