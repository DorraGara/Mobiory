package com.example.mobiory.data.local

import android.content.Context
import android.util.Log
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mobiory.R
import com.example.mobiory.data.AppDatabase
import com.example.mobiory.data.model.Event
import com.example.mobiory.data.model.Popularity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date

class PrepopulateRoomCallback(private val context: Context) : RoomDatabase.Callback() {
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd")


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
                    val event = this.parseEvent(eventObj)
                    eventDao.insert(event)

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
    private fun deleteAllData() {
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
    private fun parseEvent(eventObj: JSONObject): Event {
        val popularityObj = eventObj.optJSONObject("popularity")
        val popularityEN = popularityObj?.optInt("en", -1) ?: -1
        val popularityFR = popularityObj?.optInt("fr", -1) ?: -1

        val claimsListString = eventObj.optJSONArray("claims")
        var startDate: Date? = null
        var endDate: Date? = null

        if (claimsListString != null) {
            for (i in 0 until claimsListString.length()) {
                val claimObj = claimsListString.getJSONObject(i)
                val verboseName = claimObj.optString("verboseName", "")
                when (verboseName) {
                    "fr:date de début||en:start time" -> {
                        val value = claimObj.optString("value", "")
                        startDate = DateTypeConverter().fromStringToDate(value.substringAfter("date:")
                            .trim())
                    }

                    "fr:date de fin||en:end time" -> {
                        val value = claimObj.optString("value", "")
                        endDate = DateTypeConverter().fromStringToDate(value.substringAfter("date:")?.trim())

                    }
                }
            }
        }

        return Event(
            eventObj.getInt("id"),
            eventObj.optString("label"),
            eventObj.optString("aliases"),
            eventObj.optString("description"),
            eventObj.optString("wikipedia"),
            Popularity(popularityEN, popularityFR),
            startDate,
            endDate
        )
    }
}