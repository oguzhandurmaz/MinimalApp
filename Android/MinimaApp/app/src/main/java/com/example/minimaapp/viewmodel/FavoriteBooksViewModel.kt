package com.example.minimaapp.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.minimaapp.data.CountRoomDatabase
import com.example.minimaapp.data.table.BookTable
import com.example.minimaapp.repo.BookRepository
import kotlinx.coroutines.launch

class FavoriteBooksViewModel(application: Application): AndroidViewModel(application) {

    private var bookRepository: BookRepository


    val favBooks: LiveData<List<BookTable>>

    init {
        val bookDao = CountRoomDatabase.getDataBase(application).bookDao()
        bookRepository = BookRepository(bookDao)

        favBooks = bookRepository.get()
    }

    fun delete(position: Int){
        viewModelScope.launch {
            favBooks.value?.get(position)?.let {
                bookRepository.delete(it)
            }
        }
    }



    @Suppress("UNCHECKED_CAST")
    class FavoriteBooksViewModelFactory(private val application: Application): ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return (FavoriteBooksViewModel(application)) as T
        }
    }
}