package com.example.minimaapp

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class BookDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val _fetchedBookDetail = MutableLiveData<String>()
    val fetchedBookDetail: LiveData<String>
        get() = _fetchedBookDetail


    private var bookRepository: BookRepository

    init {
        val bookDao = CountRoomDatabase.getDataBase(application).bookDao()
        bookRepository = BookRepository(bookDao)
    }

    fun getBookDetail(url: String) =
        viewModelScope.launch { _fetchedBookDetail.value = bookRepository.getBookDetail(url) }


    fun insert(bookTable: BookTable) = viewModelScope.launch {
        bookRepository.insert(bookTable)
    }


    @Suppress("UNCHECKED_CAST")
    class BookDetailViewModelFactory(private val application: Application) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return (BookDetailViewModel(application)) as T
        }
    }
}