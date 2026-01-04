package com.example.hraukol13.repository

import com.example.hraukol13.data.*

class QuizRepository(private val db: AppDatabase) {

    private val questionDao = db.questionDao()
    private val resultDao = db.resultDao()

    fun getAllQuestions() = questionDao.getAllQuestions()
    fun getAllResults() = resultDao.getAllResults()

    suspend fun insertQuestion(question: Question.QuestionEntity) = questionDao.insert(question)
    suspend fun insertResult(result: Result.ResultEntity) = resultDao.insert(result)
    
    suspend fun deleteResult(result: Result.ResultEntity) = resultDao.delete(result)
    suspend fun deleteAllResults() = resultDao.deleteAll()
}
