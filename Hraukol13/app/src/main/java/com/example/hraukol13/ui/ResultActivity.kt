package com.example.hraukol13.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.hraukol13.R
import com.example.hraukol13.viewmodel.QuizViewModel

class ResultActivity : AppCompatActivity() {

    private lateinit var viewModel: QuizViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val score = intent.getIntExtra("score", 0)
        val total = intent.getIntExtra("total", 0)
        val name = intent.getStringExtra("playerName") ?: "Hráč"

        findViewById<TextView>(R.id.tvPlayerName).text = name
        findViewById<TextView>(R.id.tvScore).text = "Skóre: $score z $total"

        viewModel = ViewModelProvider(this).get(QuizViewModel::class.java)

        val llHistoryContainer = findViewById<LinearLayout>(R.id.llHistoryContainer)
        val btnDeleteAll = findViewById<ImageButton>(R.id.btnDeleteAll)

        // Pozorování historie a dynamické vykreslování
        viewModel.results.observe(this, Observer { results ->
            llHistoryContainer.removeAllViews()
            
            if (results.isNullOrEmpty()) {
                val emptyTv = TextView(this)
                emptyTv.text = "Zatím žádná historie..."
                llHistoryContainer.addView(emptyTv)
            } else {
                results.forEach { result ->
                    // Pro každý výsledek nafoukneme item_result.xml
                    val itemView = LayoutInflater.from(this).inflate(R.layout.item_result, llHistoryContainer, false)
                    
                    val tvResultText = itemView.findViewById<TextView>(R.id.tvResultText)
                    val btnDeleteOne = itemView.findViewById<TextView>(R.id.btnDeleteOne)

                    tvResultText.text = "${result.playerName}: ${result.score} bodů"
                    
                    // Křížek pro smazání jednoho záznamu
                    btnDeleteOne.setOnClickListener {
                        viewModel.deleteResult(result)
                    }

                    llHistoryContainer.addView(itemView)
                }
            }
        })

        // Tlačítko koše pro smazání všeho
        btnDeleteAll.setOnClickListener {
            viewModel.deleteAllResults()
        }

        findViewById<Button>(R.id.btnBack).setOnClickListener {
            finish()
        }
    }
}
