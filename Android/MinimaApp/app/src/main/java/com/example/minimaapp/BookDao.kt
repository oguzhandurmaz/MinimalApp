package com.example.minimaapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BookDao {

    @Query("SELECT * from book_table")
    fun getAllBooks(): List<BookTable>

    @Insert
    suspend fun insert(bookTable: BookTable)
}