package com.example.mobiory.data.repository

import androidx.lifecycle.LiveData
import com.example.mobiory.data.local.EventDao
import com.example.mobiory.data.model.Event
import com.example.mobiory.data.model.EventWithClaims
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EventRepository @Inject constructor(private val eventDao: EventDao) {
/*
    fun getAllEvents(): LiveData<List<Event>> {
        return eventDao.getAllEvents()
    }*/

    fun getAllEventsFlow(): Flow<List<Event>> {
        return eventDao.getAllEventsFlow()
    }
}