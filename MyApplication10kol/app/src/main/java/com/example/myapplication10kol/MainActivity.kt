package com.example.myapplication10kol

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication10kol.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val answers = listOf(
        "Ano, určitě!",
        "Bezpochyby.",
        "Můžeš se na to spolehnout.",
        "Pravděpodobně ano.",
        "Výhled je dobrý.",
        "Zeptej se později.",
        "Teď ti to neřeknu.",
        "Soustřeď se a zeptej se znovu.",
        "Moje odpověď je NE.",
        "Mé zdroje říkají, že ne.",
        "Velmi pochybné."
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAsk.setOnClickListener {
            val question = binding.etQuestion.text.toString().trim()

            if (question.isEmpty()) {
                Toast.makeText(this, "Musíš položit otázku!", Toast.LENGTH_SHORT).show()
                binding.tvAnswer.text = "..."
            } else {
                showRandomAnswer()
                binding.etQuestion.text.clear()
            }
        }
    }

    private fun showRandomAnswer() {
        val randomIndex = Random.nextInt(answers.size)
        binding.tvAnswer.text = answers[randomIndex]
    }
}