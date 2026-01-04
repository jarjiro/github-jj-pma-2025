package com.example.myapplication08

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.example.myapplication08.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)

        val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        binding.btnSave.setOnClickListener {
            val name = binding.etTextName.text.toString()
            val age = binding.etTextAge.text.toString().trim()
            val isAdult = binding.checkBoxAdult.isChecked
            if (age.isBlank()) {
                Toast.makeText(this, "Uloženo!", Toast.LENGTH_SHORT).show()
            } else {
                val age = age.toInt()
                if ((age < 18 && isAdult) || (age > 18 && isAdult)) {
                    Toast.makeText(this, "Kecáš!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "uloženo", Toast.LENGTH_SHORT).show()
                    editor.apply {
                        putString("name", name)
                        putInt("age", age)
                        putBoolean("isAdult", isAdult)
                        apply()
                    }
                }
            }

            binding.btnLoad.setOnClickListener {
                val name = sharedPref.getString("name", null)
                val age = sharedPref.getInt("age", 0)
                val isAdult = sharedPref.getBoolean("adult", false)

                binding.etTextName.setText(name)
                binding.etTextAge.setText(age)
                binding.checkBoxAdult.isChecked = isAdult

                Toast.makeText(this, "Načteno!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
