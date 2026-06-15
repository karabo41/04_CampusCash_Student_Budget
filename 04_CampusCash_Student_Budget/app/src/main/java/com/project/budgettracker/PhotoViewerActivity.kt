package com.project.budgettracker

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PhotoViewerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_viewer)

        val imageReceipt = findViewById<ImageView>(R.id.imageReceipt)
        val photoPath = intent.getStringExtra("PHOTO_PATH")

        if (photoPath.isNullOrBlank()) {
            Toast.makeText(this, "No image found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        imageReceipt.setImageURI(Uri.parse(photoPath))
    }
}