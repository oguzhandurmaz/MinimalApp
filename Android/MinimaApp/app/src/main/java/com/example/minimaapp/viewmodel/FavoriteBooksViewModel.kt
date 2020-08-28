package com.example.minimaapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.minimaapp.data.CountRoomDatabase
import com.example.minimaapp.data.table.BookTable
import com.example.minimaapp.repo.BookRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoriteBooksViewModel @Inject constructor(application: Application,private val bookRepository: BookRepository): AndroidViewModel(application) {

    //private var bookRepository: BookRepository


    val favBooks: LiveData<List<BookTable>>

    init {
      /*  val bookDao = CountRoomDatabase.getDataBase(application).bookDao()
        bookRepository = BookRepository(bookDao)*/

        favBooks = bookRepository.get()
    }

    fun delete(position: Int){
        viewModelScope.launch {
            Log.d("Minimal","VM Position: $position - favBooks size: ${favBooks.value?.size}")
            favBooks.value?.get(position)?.let {

                bookRepository.delete(it)
            }
        }
    }


/*
    @Suppress("UNCHECKED_CAST")
    class FavoriteBooksViewModelFactory(private val application: Application): ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return (FavoriteBooksViewModel(application)) as T
        }
    }*/
}