package com.learn.cancerapp.ui.history

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.learn.cancerapp.data.history.HistoryRepository
import com.learn.cancerapp.data.history.entity.HistoryEntity

class HistoryViewModel(application: Application) : ViewModel() {
    private val mHistoryRepository: HistoryRepository = HistoryRepository(application)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    init {
        _isLoading.value = false
    }

    fun insert(history: HistoryEntity) {
        mHistoryRepository.insert(history)
    }

    fun getAllHistory(): LiveData<List<HistoryEntity>> {
        return mHistoryRepository.getAllHistory()
    }
}