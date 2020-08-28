package com.example.minimaapp.di

import android.app.Application
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.minimaapp.data.CountRoomDatabase
import com.example.minimaapp.data.dao.BookDao
import com.example.minimaapp.data.dao.CountDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    companion object{

        @Singleton
        @Provides
        fun provideCountRoomDataBase(application: Application): CountRoomDatabase{
            return Room.databaseBuilder(application.applicationContext,CountRoomDatabase::class.java,"count_database")
                .fallbackToDestructiveMigration()
                .build()
        }

        @Singleton
        @Provides
        fun provideBookDao(countRoomDatabase: CountRoomDatabase): BookDao{
            return countRoomDatabase.bookDao()
        }

        @Singleton
        @Provides
        fun provideCountDao(countRoomDatabase: CountRoomDatabase): CountDao{
            return countRoomDatabase.countDao()
        }

        @Singleton
        @Provides
        fun provideGlideInstance(application: Application): RequestManager{
            return Glide.with(application.applicationContext)
        }
    }
}