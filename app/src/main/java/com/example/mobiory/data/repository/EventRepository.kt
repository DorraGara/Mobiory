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

    fun getAllEventsFlow(): Flow<List<Event>> {
        return eventDao.getAllEventsFlow()
    }

    fun getSearchedEvents(searchString : String): Flow<List<Event>> {
        return eventDao.searchEvents(searchString)
    }
}