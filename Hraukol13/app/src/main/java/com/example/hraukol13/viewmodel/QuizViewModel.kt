package com.example.hraukol13.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.hraukol13.data.*
import com.example.hraukol13.repository.QuizRepository
import kotlinx.coroutines.launch

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = QuizRepository(AppDatabase.getDatabase(application))

    val allQuestions: LiveData<List<Question.QuestionEntity>> = repository.getAllQuestions()
    val results: LiveData<List<Result.ResultEntity>> = repository.getAllResults()

    fun saveResult(playerName: String, score: Int) = viewModelScope.launch {
        repository.insertResult(
            Result.ResultEntity(
                playerName = playerName,
                score = score
            )
        )
    }

    fun deleteResult(result: Result.ResultEntity) = viewModelScope.launch {
        repository.deleteResult(result)
    }

    fun deleteAllResults() = viewModelScope.launch {
        repository.deleteAllResults()
    }
}
