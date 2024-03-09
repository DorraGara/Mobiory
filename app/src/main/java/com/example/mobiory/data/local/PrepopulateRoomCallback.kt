package com.example.mobiory.data.local

import android.content.Context
import android.util.Log
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mobiory.R
import com.example.mobiory.data.AppDatabase
import com.example.mobiory.data.model.Description
import com.example.mobiory.data.model.Event
import com.example.mobiory.data.model.Label
import com.example.mobiory.data.model.Popularity
import com.example.mobiory.data.model.Wikipedia
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.util.Date

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
                    val event = this.parseEvent(eventObj)
                    if (event != null ) eventDao.insert(event)
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
    private fun parseEvent(eventObj: JSONObject): Event? {
        val popularityObj = eventObj.optJSONObject("popularity")
        val popularityEN = popularityObj?.optInt("en", -1) ?: -1
        val popularityFR = popularityObj?.optInt("fr", -1) ?: -1

        val labelObj = eventObj.optString("label").split("||")
        var labelEN :  String? = null
        var labelFR :  String? = null
        for (str in labelObj) {
            val lgStr = str.split(":")
            if (lgStr.size == 2) {
                when (lgStr[0]) {
                    "en" -> labelEN = lgStr[1]
                    "fr" -> labelFR = lgStr[1]
                }
            }
        }

        val descriptionObj = eventObj.optString("label").split("||")
        var descriptionEN :  String? = null
        var descriptionFR :  String? = null
        for (str in descriptionObj) {
            val lgStr = str.split(":")
            if (lgStr.size == 2) {
                when (lgStr[0]) {
                    "en" -> descriptionEN = lgStr[1]
                    "fr" -> descriptionFR = lgStr[1]
                }
            }
        }

        val wikipediaObj = eventObj.optString("label").split("||")
        var wikipediaEN :  String? = null
        var wikipediaFR :  String? = null
        for (str in wikipediaObj) {
            val lgStr = str.split(":")
            if (lgStr.size == 2) {
                when (lgStr[0]) {
                    "en" -> wikipediaEN = lgStr[1]
                    "fr" -> wikipediaFR = lgStr[1]
                }
            }
        }
        val claimsListString = eventObj.optJSONArray("claims")
        var startDate: Date? = null
        var endDate: Date? = null
        var pointInTime: Date? = null

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
                        endDate = DateTypeConverter().fromStringToDate(value.substringAfter("date:").trim())

                    }

                    "fr:date||en:point in time" -> {
                        val value = claimObj.optString("value", "")
                        pointInTime = DateTypeConverter().fromStringToDate(value.substringAfter("date:").trim())

                    }
                }
            }
        }
        return if ((startDate == null) and (endDate == null) and (pointInTime == null)) {
            null
        } else {
            Event(
                eventObj.getInt("id"),
                Label(labelEN,labelFR),
                Description(descriptionEN,descriptionFR),
                Wikipedia(wikipediaEN,wikipediaFR),
                Popularity(popularityEN, popularityFR),
                startDate,
                endDate,
                pointInTime
            )
        }
    }
}