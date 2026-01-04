package com.example.hraukol13.data

import androidx.room.Entity
import androidx.room.PrimaryKey

class Question {

    @Entity(tableName = "questions")
    data class QuestionEntity(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        val questionText: String,
        val correctAnswer: String,
        val wrongAnswer1: String,
        val wrongAnswer2: String
    )
}
