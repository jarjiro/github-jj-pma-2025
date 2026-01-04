package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SecActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sec2)

        val tvInfo = findViewById<TextView>(R.id.tvInfo)

        val name = intent.getStringExtra("name")
        tvInfo.text = "Data z první aktivity. Name: $name"
        val btnClose = findViewById<Button>(R.id.btnClose)
        btnClose.setOnClickListener {
            finish()
        }
        }
    }
