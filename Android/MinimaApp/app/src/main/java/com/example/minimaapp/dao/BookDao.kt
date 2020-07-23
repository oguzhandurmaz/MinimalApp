package com.example.minimaapp.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.minimaapp.BookTable

@Dao
interface BookDao {

    @Query("SELECT * from book_table")
    fun getAllBooks(): List<BookTable>

    @Insert
    suspend fun insert(bookTable: BookTable)
}