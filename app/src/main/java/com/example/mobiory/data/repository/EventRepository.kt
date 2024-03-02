package com.example.mobiory.data.repository

import androidx.lifecycle.LiveData
import com.example.mobiory.data.local.EventDao
import com.example.mobiory.data.model.Event
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class EventRepository @Inject constructor(private val eventDao: EventDao) {

    // LiveData for the list of events
    fun getAllEvents(): LiveData<List<Event>> {
        return eventDao.getAllEvents()
    }

    fun getAllEventsFlow(): Flow<List<Event>> {
        return eventDao.getAllEventsFlow()
    }

    // Function to import events from a file
    /*suspend fun importEventsFromTxt(file: File) {
        withContext(Dispatchers.IO) {
            // Code to read and parse the file, then insert events into the database
            println("Contenu du fichier : ${file.name}")
            val json = file.readText()
            val events = Gson().fromJson(json, Array<Event>::class.java).toList()
            eventDao.insertAll(events)
        }
    }*/
}