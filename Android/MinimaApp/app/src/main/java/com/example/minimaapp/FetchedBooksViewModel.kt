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

    private var countRepository: CountRepository

    init {
        val countDao = CountRoomDatabase.getDataBase(application).countDao()

        countRepository = CountRepository(countDao)
    }

    fun getBooks() = viewModelScope.launch{
        _books.value = countRepository.getBooks()
    }




    @Suppress("UNCHECKED_CAST")
    class FetchedBooksViewModelFactory(private val application: Application): ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return (FetchedBooksViewModel(application)) as T
        }
    }
}