package com.example.minimaapp.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.minimaapp.data.CountRoomDatabase
import com.example.minimaapp.data.table.BookTable
import com.example.minimaapp.repo.BookRepository
import kotlinx.coroutines.launch

class BookDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val _fetchedBookDetail = MutableLiveData<String>()
    val fetchedBookDetail: LiveData<String>
        get() = _fetchedBookDetail
    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean>
        get() = _success

    val favBooks: LiveData<List<BookTable>>


    private var bookRepository: BookRepository

    init {
        val bookDao = CountRoomDatabase.getDataBase(application).bookDao()
        bookRepository = BookRepository(bookDao)

        favBooks = bookRepository.get()
    }

    fun getBookDetail(url: String) =
        viewModelScope.launch { _fetchedBookDetail.value = bookRepository.getBookDetail(url) }

    fun setBookDetail(detail: String){
        _fetchedBookDetail.value = detail
    }


    fun insert(bookTable: BookTable) = viewModelScope.launch {
        _success.value =  bookRepository.insert(bookTable)
    }


    @Suppress("UNCHECKED_CAST")
    class BookDetailViewModelFactory(private val application: Application) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return (BookDetailViewModel(application)) as T
        }
    }
}