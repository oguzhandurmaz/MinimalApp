package com.example.minimaapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.minimaapp.data.table.Count

@Dao
interface CountDao {
    @Query("SELECT * from count_table")
    suspend fun getAllData(): List<Count>

    @Query("SELECT * from count_table ORDER BY id DESC LIMIT 7")
    fun getLastSevenData(): LiveData<List<Count>>

    @Insert
    suspend fun insert(count: Count)


}