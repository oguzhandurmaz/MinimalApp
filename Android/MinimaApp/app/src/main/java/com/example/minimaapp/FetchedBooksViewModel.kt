package com.example.minimaapp

import android.app.Application
import androidx.lifecycle.*
import androidx.room.RoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FetchedBooksViewModel(application: Application): AndroidViewModel(application) {
    private val _books = MutableLiveData<List<Book>>()
    val books: LiveData<List<Book>>
        get() = _books

    private var bookRepository: BookRepository

    init {
        val bookDao = CountRoomDatabase.getDataBase(application).bookDao()

        bookRepository= BookRepository(bookDao)
    }

    fun getBooks() = viewModelScope.launch{
        _books.value = bookRepository.fetchBooks()
    }




    @Suppress("UNCHECKED_CAST")
    class FetchedBooksViewModelFactory(private val application: Application): ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return (FetchedBooksViewModel(application)) as T
        }
    }
}