package com.example.minimaapp.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.minimaapp.data.CountRoomDatabase
import com.example.minimaapp.data.table.Count
import com.example.minimaapp.repo.CountRepository
import kotlinx.coroutines.launch

class GraphicsFragmentViewModel(application: Application): AndroidViewModel(application) {

    private var countRepository: CountRepository

    val countDataSeven: LiveData<List<Count>>

    init{
        val countDao = CountRoomDatabase.getDataBase(application).countDao()
        countRepository = CountRepository(countDao)

        countDataSeven = countRepository.countSeven
    }


    @Suppress("UNCHECKED_CAST")
    class GraphicsFragmentViewModelFactory(val application: Application): ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return (GraphicsFragmentViewModel(application)) as T
        }
    }
}