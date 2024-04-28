package com.learn.cancerapp.data.history

import android.app.Application
import androidx.lifecycle.LiveData
import com.learn.cancerapp.data.history.entity.HistoryEntity
import com.learn.cancerapp.data.history.room.HistoryDao
import com.learn.cancerapp.data.history.room.HistoryDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class HistoryRepository(application: Application) {
    private val mHistoryDao: HistoryDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = HistoryDatabase.getDatabase(application)
        mHistoryDao = db.historyDao()
    }

    fun getAllHistory(): LiveData<List<HistoryEntity>> = mHistoryDao.getAllHistory()

    fun insert(history: HistoryEntity) {
        executorService.execute { mHistoryDao.insert(history) }
    }
}