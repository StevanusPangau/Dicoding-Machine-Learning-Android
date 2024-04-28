package com.learn.cancerapp.data.history.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "history")
@Parcelize
class HistoryEntity(
    @PrimaryKey(autoGenerate = true)

    @field:ColumnInfo(name = "id")
    var id: Int = 0,

    @field:ColumnInfo(name = "hasilPrediksi")
    var hasilPrediksi: String? = null,

    @field:ColumnInfo(name = "gambar")
    var gambar: String = "",

    @ColumnInfo(name = "tanggal")
    var tanggal: Long = System.currentTimeMillis()
) : Parcelable