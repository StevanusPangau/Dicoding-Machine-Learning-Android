package com.learn.cancerapp.data.history.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.learn.cancerapp.data.history.entity.HistoryEntity

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history")
    fun getAllHistory(): LiveData<List<HistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: HistoryEntity)
}