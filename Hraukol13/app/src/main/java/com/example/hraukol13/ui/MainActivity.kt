package com.example.hraukol13.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hraukol13.R
import com.example.hraukol13.data.AppDatabase
import com.example.hraukol13.data.DemoData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TADY BYLA CHYBA: Musí být etPlayerName, protože tak je to v XML
        val etName = findViewById<EditText>(R.id.etPlayerName)
        val btnStart = findViewById<Button>(R.id.btnStart)

        // Pojistka: Vložení dat hned při startu, pokud chybí
        val db = AppDatabase.getDatabase(this)
        CoroutineScope(Dispatchers.IO).launch {
            val questions = db.questionDao().getAllQuestionsRaw()
            if (questions.isEmpty()) {
                DemoData.questions.forEach {
                    db.questionDao().insert(it)
                }
            }
        }

        btnStart.setOnClickListener {
            val name = etName.text.toString()
            if (name.isNotEmpty()) {
                // Uložíme jméno do SharedPreferences
                getSharedPreferences("quiz_prefs", Context.MODE_PRIVATE)
                    .edit()
                    .putString("player_name", name)
                    .apply()

                val intent = Intent(this, GameActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Zadej jméno!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
