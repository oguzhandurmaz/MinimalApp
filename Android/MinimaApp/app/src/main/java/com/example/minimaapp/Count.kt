package com.example.minimaapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "count_table")
data class Count(
    @PrimaryKey(autoGenerate = true)
    var id: Int= 0,
    var date: String,
    var count: Int,
    var time: Int
)