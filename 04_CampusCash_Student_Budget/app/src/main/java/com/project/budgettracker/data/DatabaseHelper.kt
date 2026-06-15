package com.project.budgettracker.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "smart_budget.db"
        private const val DATABASE_VERSION = 1
        private const val TAG = "DatabaseHelper"

        const val TABLE_USERS = "users"
        const val TABLE_CATEGORIES = "categories"
        const val TABLE_EXPENSES = "expenses"
        const val TABLE_GOALS = "goals"
        const val TABLE_CATEGORY_LIMITS = "category_limits"
        const val TABLE_BADGES = "badges"
        const val TABLE_REMINDERS = "reminders"
        const val TABLE_SAVING_TIPS = "saving_tips"
    }

    override fun onCreate(db: SQLiteDatabase) {
        Log.d(TAG, "Creating database tables")

        db.execSQL("""
            CREATE TABLE $TABLE_USERS (
                user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL UNIQUE,
                password TEXT NOT NULL
            )
        """)

        db.execSQL("""
            CREATE TABLE $TABLE_CATEGORIES (
                category_id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL,
                category_name TEXT NOT NULL,
                FOREIGN KEY(user_id) REFERENCES $TABLE_USERS(user_id)
            )
        """)

        db.execSQL("""
            CREATE TABLE $TABLE_EXPENSES (
                expense_id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL,
                category_id INTEGER NOT NULL,
                amount REAL NOT NULL,
                expense_date TEXT NOT NULL,
                description TEXT NOT NULL,
                photo_path TEXT,
                FOREIGN KEY(user_id) REFERENCES $TABLE_USERS(user_id),
                FOREIGN KEY(category_id) REFERENCES $TABLE_CATEGORIES(category_id)
            )
        """)

        db.execSQL("""
            CREATE TABLE $TABLE_GOALS (
                goal_id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL,
                month_year TEXT NOT NULL,
                min_goal REAL NOT NULL,
                max_goal REAL NOT NULL,
                FOREIGN KEY(user_id) REFERENCES $TABLE_USERS(user_id)
            )
        """)

        db.execSQL("""
            CREATE TABLE $TABLE_CATEGORY_LIMITS (
                limit_id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL,
                category_id INTEGER NOT NULL,
                limit_amount REAL NOT NULL,
                FOREIGN KEY(user_id) REFERENCES $TABLE_USERS(user_id),
                FOREIGN KEY(category_id) REFERENCES $TABLE_CATEGORIES(category_id)
            )
        """)

        db.execSQL("""
            CREATE TABLE $TABLE_BADGES (
                badge_id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL,
                badge_name TEXT NOT NULL,
                badge_description TEXT NOT NULL,
                earned_date TEXT NOT NULL,
                FOREIGN KEY(user_id) REFERENCES $TABLE_USERS(user_id)
            )
        """)

        db.execSQL("""
            CREATE TABLE $TABLE_REMINDERS (
                reminder_id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL,
                title TEXT NOT NULL,
                due_date TEXT NOT NULL,
                note TEXT,
                FOREIGN KEY(user_id) REFERENCES $TABLE_USERS(user_id)
            )
        """)

        db.execSQL("""
            CREATE TABLE $TABLE_SAVING_TIPS (
                tip_id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL,
                tip_text TEXT NOT NULL,
                saved_date TEXT NOT NULL,
                FOREIGN KEY(user_id) REFERENCES $TABLE_USERS(user_id)
            )
        """)

        Log.d(TAG, "Database tables created successfully")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.d(TAG, "Upgrading database from version $oldVersion to $newVersion")

        db.execSQL("DROP TABLE IF EXISTS $TABLE_SAVING_TIPS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_REMINDERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_BADGES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CATEGORY_LIMITS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_GOALS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EXPENSES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CATEGORIES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")

        onCreate(db)
    }
}