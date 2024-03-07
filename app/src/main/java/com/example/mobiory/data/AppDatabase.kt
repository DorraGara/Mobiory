package com.example.mobiory.data

import android.content.Context
import androidx.room.Room
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mobiory.data.local.EventDao
import com.example.mobiory.data.local.PrepopulateRoomCallback
import com.example.mobiory.data.model.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Event::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // Create new database instance or return the existing one.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "app_database")
                    .addCallback(PrepopulateRoomCallback(context))
                    .build()
                    .also { Instance = it }
            }
        }
        fun updatetDatabase(context: Context) {
            CoroutineScope(Dispatchers.IO).launch {
                PrepopulateRoomCallback(context).updateDatabase()
            }
        }
    }
}