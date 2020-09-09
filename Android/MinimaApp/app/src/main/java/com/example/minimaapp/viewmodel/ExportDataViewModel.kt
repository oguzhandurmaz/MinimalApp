package com.example.minimaapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.minimaapp.data.table.Count
import com.example.minimaapp.repo.CountRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class ExportDataViewModel @Inject constructor(application: Application, private val countRepository: CountRepository) : AndroidViewModel(application){
    private val _allData = MutableLiveData<List<Count>>()
    val allData: LiveData<List<Count>>
        get() = _allData



    fun getAllData() = viewModelScope.launch {
        _allData.value = countRepository.getAllData()
    }
}