package com.example.mobiory.data.local

import android.content.Context
import android.util.Log
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mobiory.R
import com.example.mobiory.data.AppDatabase
import com.example.mobiory.data.model.Claim
import com.example.mobiory.data.model.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
class PrepopulateRoomCallback(private val context: Context) : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        CoroutineScope(Dispatchers.IO).launch {
            prePopulateEvents()
        }
    }

    suspend fun prePopulateEvents() {
        Log.i("prePopulateEvents database", "prePopulateEvents: entered ")

        try {
            val eventDao = AppDatabase.getDatabase(context).eventDao()
            val eventsList: JSONArray =
                context.resources.openRawResource(R.raw.events).bufferedReader().use {
                    JSONArray(it.readText())
                }

            /*
            val eventsArray = JSONArray(eventsJsonString)
            for (index in 0 until eventsArray.length()) {
                val eventObj = eventsArray.getJSONObject(index)
                Log.i("Prepopulate data", "event Obj : $eventObj")
                val event = convertJsonToEvent(eventObj)
                insertEventAndReferences(eventDao, event)
            }*/
            eventsList.takeIf { it.length() > 0 }?.let { list ->
                for (index in 0 until list.length()) {
                    val eventObj = list.getJSONObject(index)

                    /*eventDao.insert(
                        Event(
                            eventObj.getInt("id"),
                            eventObj.getString("label"),
                            eventObj.getString("aliases"),
                            eventObj.getString("description"),
                            eventObj.getString("wikipedia"),
                            null,
                            null,
                        )
                    )*/
                    Log.i("Prepopulate data", "event Obj : $eventObj")
                    val (event,claims) = convertJsonToEvent(eventObj)
                    insertEventAndReferences(eventDao, event, claims)
                }
            }
            Log.i("Prepopulate data", "Success")
        } catch (exception: Exception) {
            Log.e(
                "Prepopulate data",
                exception.localizedMessage ?: "Fail"
            )
        }
    }
    suspend fun updateDatabase() {
        Log.i("update database", "update: entered ")

        try {
            val eventDao = AppDatabase.getDatabase(context).eventDao()
            eventDao.deleteAll()
            Log.i("update database", "all deleted ")

            val eventsList: JSONArray =
                context.resources.openRawResource(R.raw.events).bufferedReader().use {
                    JSONArray(it.readText())
                }
            eventsList.takeIf { it.length() > 0 }?.let { list ->
                for (index in 0 until list.length()) {
                    val eventObj = list.getJSONObject(index)
                    /*
                    eventDao.insert(
                        Event(
                            eventObj.getInt("id"),
                            eventObj.getString("label"),
                            eventObj.getString("aliases"),
                            eventObj.getString("description"),
                            eventObj.getString("wikipedia"),
                            null,
                            null,
                        )
                    )*/
                    Log.i("update database", "event Obj : $eventObj")

                    /*val event = convertJsonToEvent(eventObj)
                    insertEventAndReferences(eventDao, event) */
                }
            }
            Log.i("update data", "Success")
        } catch (exception: Exception) {
            Log.e(
                "update data",
                exception.localizedMessage ?: "Fail"
            )
        }
    }

    private suspend fun insertEventAndReferences(eventDao: EventDao, event: Event, claims: List<Claim>) {
        if (!eventDao.exists(event.eventId)) {
            for (claim in event.claims.orEmpty()) {
                insertEventAndReferences(
                    eventDao,
                    claim.event
                )
            }
            eventDao.insert(event)
            Log.i("Mobiory App", "Inserted event with id ${event.eventId} into database")
        } else {
            Log.i(
                "Mobiory App",
                "Event with id ${event.eventId} already exists in the database. Skipping insertion."
            )
        }
    }

    private fun convertJsonToEvent(eventObj: JSONObject): Pair<Event,List<Claim>> {
        return EventConverters().fromEventString(eventObj.toString())
    }
}