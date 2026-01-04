package com.example.hraukol13.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface QuestionDao {

    @Query("SELECT * FROM questions ORDER BY RANDOM() LIMIT 1")
    fun getRandomQuestion(): LiveData<Question.QuestionEntity>

    @Query("SELECT * FROM questions")
    fun getAllQuestions(): LiveData<List<Question.QuestionEntity>>

    @Query("SELECT * FROM questions")
    suspend fun getAllQuestionsRaw(): List<Question.QuestionEntity>

    @Insert
    suspend fun insert(question: Question.QuestionEntity)
}
