package com.example.myapplication012

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication012.databinding.ActivitySummaryBinding

class SummaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySummaryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Přečteme summary z intentu
        val summary = intent.getStringExtra("summary") ?: "Žádná objednávka"

        // Zobrazíme v TextView
        binding.tvSummaryDetail.text = summary
    }
}


