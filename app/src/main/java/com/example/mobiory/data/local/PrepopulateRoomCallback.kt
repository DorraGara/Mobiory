package com.example.mobiory.data.local

import android.content.Context
import android.util.Log
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mobiory.R
import com.example.mobiory.data.AppDatabase
import com.example.mobiory.data.model.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray

class PrepopulateRoomCallback(private val context: Context) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        CoroutineScope(Dispatchers.IO).launch {
            prePopulateEvents()
        }
    }

    private suspend fun prePopulateEvents() {
        try {
            val eventDao = AppDatabase.getDatabase(context).eventDao()

            val eventsList: JSONArray =
                context.resources.openRawResource(R.raw.events).bufferedReader().use {
                    JSONArray(it.readText())
                }
            eventsList.takeIf { it.length() > 0 }?.let { list ->
                for (index in 0 until list.length()) {
                    val eventObj = list.getJSONObject(index)
                    eventDao.insert(
                        Event(
                            eventObj.getInt("id"),
                            eventObj.getString("label"),
                            eventObj.getString("aliases"),
                            eventObj.getString("description"),
                            eventObj.getString("wikipedia")
                        )
                    )

                }
                Log.i("Mobiory App", "successfully pre-populated events into database")
            }
        } catch (exception: Exception) {
            Log.e(
                "Mobiory App import data error",
                exception.localizedMessage ?: "failed to pre-populate events into database"
            )
        }
    }
    private suspend fun deleteAllData() {
        try {
            val eventDao = AppDatabase.getDatabase(context).eventDao()
            eventDao.deleteAll()
            Log.i("Mobiory App delete data error", "all deleted ")

        } catch (exception: Exception) {
            Log.e(
                "Mobiory App delete data error",
                exception.localizedMessage ?: "failed to delete events from database"
            )
        }
    }
    suspend fun updateDatabase() {
        this.deleteAllData()
        this.prePopulateEvents()
    }
}