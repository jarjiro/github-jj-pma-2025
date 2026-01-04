package com.example.hraukol13.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.hraukol13.R
import com.example.hraukol13.data.Question
import com.example.hraukol13.viewmodel.QuizViewModel

class GameActivity : AppCompatActivity() {

    private lateinit var tvQuestion: TextView
    private lateinit var btns: List<Button>
    private lateinit var playerName: String
    private var score = 0
    private var currentIndex = 0
    private var questionsList: List<Question.QuestionEntity> = emptyList()

    private lateinit var viewModel: QuizViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        tvQuestion = findViewById(R.id.tvQuestion)
        btns = listOf(
            findViewById(R.id.btnAnswer1),
            findViewById(R.id.btnAnswer2),
            findViewById(R.id.btnAnswer3)
        )

        playerName = getSharedPreferences("quiz_prefs", Context.MODE_PRIVATE)
            .getString("player_name", "") ?: "Hráč"

        viewModel = ViewModelProvider(this).get(QuizViewModel::class.java)

        viewModel.allQuestions.observe(this, Observer { list ->
            if (list.isNullOrEmpty()) {
                Toast.makeText(this, "Žádné otázky!", Toast.LENGTH_LONG).show()
                return@Observer
            }

            if (questionsList.isEmpty()) {
                questionsList = list.shuffled()
                currentIndex = 0
                showQuestion()
            }
        })
    }

    private fun showQuestion() {
        if (currentIndex >= questionsList.size) {
            viewModel.saveResult(playerName, score)

            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("score", score)
            intent.putExtra("total", questionsList.size)
            intent.putExtra("playerName", playerName)
            startActivity(intent)
            finish()
            return
        }

        val question = questionsList[currentIndex]
        tvQuestion.text = question.questionText

        val answers = listOf(
            question.correctAnswer,
            question.wrongAnswer1,
            question.wrongAnswer2
        ).shuffled()

        btns.forEachIndexed { index, button ->
            if (index < answers.size) {
                button.text = answers[index]
                button.setOnClickListener {
                    if (button.text == question.correctAnswer) {
                        score++
                    }
                    currentIndex++
                    showQuestion()
                }
            }
        }
    }
}
