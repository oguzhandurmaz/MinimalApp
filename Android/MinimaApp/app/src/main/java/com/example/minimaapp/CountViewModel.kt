package com.example.minimaapp

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class CountViewModel(application: Application): AndroidViewModel(application) {
    private var countRepository: CountRepository

    val countDataSeven: LiveData<List<Count>>

    init{
        val countDao = CountRoomDatabase.getDataBase(application).countDao()
        countRepository = CountRepository(countDao)

            countDataSeven = countRepository.countSeven


    }

    fun getDataSeven(){
       /* viewModelScope.launch {
            _countDataSeven.value = countRepository.getDataSeven()
        }*/
    }

    fun insert(count: Count) = viewModelScope.launch {
        countRepository.insert(count)
    }


    @Suppress("UNCHECKED_CAST")
    class CountViewModelFactory(private val application: Application): ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return (CountViewModel(application)) as T
        }
    }


}