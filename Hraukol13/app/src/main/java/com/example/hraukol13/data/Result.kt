package com.example.hraukol13.data

import androidx.room.Entity
import androidx.room.PrimaryKey

class Result {

    @Entity(tableName = "results")
    data class ResultEntity(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        val playerName: String,
        val score: Int,
        val createdAt: Long = System.currentTimeMillis()
    )
}
