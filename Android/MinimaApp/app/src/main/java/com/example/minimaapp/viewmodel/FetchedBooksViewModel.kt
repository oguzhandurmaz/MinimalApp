package com.example.minimaapp.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.minimaapp.data.Book
import com.example.minimaapp.data.CountRoomDatabase
import com.example.minimaapp.repo.BookRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class FetchedBooksViewModel @Inject constructor(application: Application,private val bookRepository: BookRepository): AndroidViewModel(application) {
    private val _books = MutableLiveData<List<Book>>()
    val books: LiveData<List<Book>>
        get() = _books

   // private var bookRepository: BookRepository

    init {
       // val bookDao = CountRoomDatabase.getDataBase(application).bookDao()

        //bookRepository= BookRepository(bookDao)
    }

    fun getBooks() = viewModelScope.launch{
        _books.value = bookRepository.fetchBooks()
    }




    /*@Suppress("UNCHECKED_CAST")
    class FetchedBooksViewModelFactory(private val application: Application): ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return (FetchedBooksViewModel(application)) as T
        }
    }*/
}