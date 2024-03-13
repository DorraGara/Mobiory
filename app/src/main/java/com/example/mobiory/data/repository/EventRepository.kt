package com.example.mobiory.data.repository

import com.example.mobiory.data.local.EventDao
import com.example.mobiory.data.model.Event
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EventRepository @Inject constructor(private val eventDao: EventDao) {

    fun getAllEventsFlow(): Flow<List<Event>> {
        return eventDao.getAllEventsFlow()
    }

    fun getSearchedEvents(searchString : String): Flow<List<Event>> {
        return eventDao.searchEvents(searchString)
    }

    fun getSortedEvents(option : String, order: String): Flow<List<Event>> {
        return if (option == "popularity")
            if (order == "asc")
                eventDao.sortEventsPopularityASC()
            else
                eventDao.sortEventsPopularityDESC()
        else
            if (order == "asc")
                eventDao.sortEventsDateASC()
            else
                eventDao.sortEventsDateDESC()

    }
}