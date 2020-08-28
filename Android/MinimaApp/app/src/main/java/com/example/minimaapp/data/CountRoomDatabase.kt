package com.example.minimaapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.minimaapp.data.dao.BookDao
import com.example.minimaapp.data.dao.CountDao
import com.example.minimaapp.data.table.BookTable
import com.example.minimaapp.data.table.Count
import javax.inject.Inject

@Database(entities = [Count::class, BookTable::class],version = 1,exportSchema = true)
abstract class CountRoomDatabase: RoomDatabase() {

    abstract fun countDao(): CountDao
    abstract fun bookDao(): BookDao

    companion object{

        @Volatile
        private var INSTANCE: CountRoomDatabase? = null

        fun getDataBase(context: Context): CountRoomDatabase {
            val tempIns = INSTANCE
            if(tempIns != null ){
                return tempIns
            }

            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CountRoomDatabase::class.java,
                    "count_databse")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }

        }



    }


}