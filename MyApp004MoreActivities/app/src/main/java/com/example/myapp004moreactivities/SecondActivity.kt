package com.example.myapp004moreactivities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapp004moreactivities.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Druhá Aktivita"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Načtení dat z intentu
        val nickname = intent.getStringExtra("NICK_NAME")
        val age = intent.getStringExtra("AGE")
        binding.twInfo.text = "Přezdívka: $nickname, Věk: $age"

        binding.btnClose.setOnClickListener {
            finish()
        }

        binding.btnThirdAct.setOnClickListener {
            val location = binding.etlocation.text.toString()
            val intent = Intent(this, ThirdActivity::class.java)
            intent.putExtra("location", location)
            startActivity(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}