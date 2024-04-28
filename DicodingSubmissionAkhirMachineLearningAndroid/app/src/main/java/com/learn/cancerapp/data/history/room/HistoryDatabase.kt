package com.learn.cancerapp.data.history.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.learn.cancerapp.data.history.entity.HistoryEntity

@Database(entities = [HistoryEntity::class], version = 2)
abstract class HistoryDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile
        private var INSTANCE: HistoryDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): HistoryDatabase {
            if (INSTANCE == null) {
                synchronized(HistoryDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        HistoryDatabase::class.java, "history_database"
                    ).build()
                }
            }
            return INSTANCE as HistoryDatabase
        }

        // Migrasi dari versi 1 ke versi 2, tambahkan code ini sebelum .build() .addMigrations(MIGRATION_1_2)
//        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                // Add the new column without a default value
//                database.execSQL("ALTER TABLE history ADD COLUMN tanggal INTEGER NOT NULL DEFAULT 0")
//
//                // Update existing rows to set the default value
//                database.execSQL("UPDATE history SET tanggal = CURRENT_TIMESTAMP WHERE tanggal = 0")
//            }
//        }
    }
}