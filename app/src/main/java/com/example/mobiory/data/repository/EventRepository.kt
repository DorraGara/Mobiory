package com.example.mobiory.data.repository

import com.example.mobiory.data.local.EventDao
import com.example.mobiory.data.model.Event
import kotlinx.coroutines.flow.Flow
import java.util.Date
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
    fun getFilteredEventsDate(startPopularity:Int, endPopularity:Int, startDate: Date, endDate: Date, country:String): Flow<List<Event>>{
        return if (country == "") {
            if ((startPopularity == 0)  and  (endPopularity == 700000))
                eventDao.searchByDateRange(startDate,endDate)
            else eventDao.searchByDateAndPopularity(startDate,endDate, startPopularity,endPopularity)
        } else {
            if ((startPopularity == 0)  and  (endPopularity == 700000))
                eventDao.searchByDateAndCountry(startDate,endDate,country)
            else eventDao.searchByDateAndPopularityAndCountry(startDate,endDate,startPopularity,endPopularity,country)
        }
    }
    fun getFilteredEvents(startPopularity:Int, endPopularity:Int, country:String): Flow<List<Event>> {
        return if (country == "") {
            if ((startPopularity == 0)  and  (endPopularity == 700000))
                eventDao.getAllEventsFlow()
            else eventDao.searchByPopularityRange(startPopularity,endPopularity)
        } else {
            if ((startPopularity == 0)  and  (endPopularity == 700000))
                eventDao.searchByCountry(country)
            else eventDao.searchByPopularityAndCountry(startPopularity,endPopularity,country)
        }
    }
}