package com.example.minimaapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.minimaapp.data.table.BookTable

@Dao
interface BookDao {

    @Query("SELECT * from book_table")
    fun getAllBooks(): LiveData<List<BookTable>>

    @Insert
    suspend fun insert(bookTable: BookTable)

    @Delete
    suspend fun delete(bookTable: BookTable)
}