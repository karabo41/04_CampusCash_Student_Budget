package com.project.budgettracker

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.project.budgettracker.data.BadgeManager
import com.project.budgettracker.utils.SessionManager

class BadgesActivity : AppCompatActivity() {

    private lateinit var badgeManager: BadgeManager
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_badges)

        badgeManager = BadgeManager(this)
        sessionManager = SessionManager(this)

        val listBadges = findViewById<ListView>(R.id.listBadges)

        val badges = badgeManager.getBadges(sessionManager.getUserId())

        if (badges.isEmpty()) {
            badges.add("No badges earned yet. Start tracking expenses.")
        }

        listBadges.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            badges
        )
    }
}