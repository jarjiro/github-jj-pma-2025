package com.example.hraukol13.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ResultDao {

    @Insert
    suspend fun insert(result: Result.ResultEntity)

    @Query("SELECT * FROM results ORDER BY createdAt DESC")
    fun getAllResults(): LiveData<List<Result.ResultEntity>>

    @Delete
    suspend fun delete(result: Result.ResultEntity)

    @Query("DELETE FROM results")
    suspend fun deleteAll()
}
