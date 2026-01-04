package com.example.myapp004moreactivities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapp004moreactivities.databinding.ActivityThirdBinding

class ThirdActivity : AppCompatActivity() {

    private lateinit var binding: ActivityThirdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThirdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Třetí Aktivita"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Načtení dat z intentu
        val location = intent.getStringExtra("location")
        binding.twInfo.text = "Data z druhé aktivity. Poloha: $location"

        binding.btnClose.setOnClickListener {
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}