package com.example.mobiory.data

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mobiory.data.local.ClaimConverter
import com.example.mobiory.data.local.EventConverters
import com.example.mobiory.data.local.EventDao
import com.example.mobiory.data.local.PrepopulateRoomCallback
import com.example.mobiory.data.model.Claim
import com.example.mobiory.data.model.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Event::class, Claim::class], version = 1)
@TypeConverters(value = [EventConverters::class, ClaimConverter::class])

abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            Log.i("init database", "getDatabase: entered ")
            return Instance ?: synchronized(this) {
                Log.i("build database", "getDatabase: entered build ")
                Room.databaseBuilder(context, AppDatabase::class.java, "app_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .addCallback(PrepopulateRoomCallback(context))
                    .build()
                    .also { Instance = it }
            }
        }

        fun updatetDatabase(context: Context) {
            Log.i("update database", "updatetDatabase: entered ")
            /*if (Instance != null) {
                Instance!!.close()
                Instance = null
            }
            return getDatabase(context)*/
            CoroutineScope(Dispatchers.IO).launch {
                PrepopulateRoomCallback(context).updateDatabase()
            }
        }
    }
}