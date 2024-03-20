package com.example.mobiory.ui.viewModel

import androidx.lifecycle.ViewModel
import com.example.mobiory.data.model.Event
import com.example.mobiory.data.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class EventListViewModel @Inject constructor (private val eventRepository: EventRepository) : ViewModel() {

    val eventList get() =  eventRepository.getAllEventsFlow()

    fun getSearchedEvents(searchString: String): Flow<List<Event>> {
        return eventRepository.getSearchedEvents(searchString)
    }
    fun getSortedEvents(option: String, order:String): Flow<List<Event>> {
        return eventRepository.getSortedEvents(option, order)
    }
    fun getFilteredEvents(startPopularity:Float, endPopularity:Float, startDate: Long?, endDate: Long?, country:String): Flow<List<Event>> {
        return if ((startDate != null) and (endDate != null))
            eventRepository.getFilteredEventsDate(startPopularity.toInt(), endPopularity.toInt(), Date(startDate!!),Date(endDate!!), country)
        else eventRepository.getFilteredEvents(startPopularity.toInt(), endPopularity.toInt(), country)
    }
}