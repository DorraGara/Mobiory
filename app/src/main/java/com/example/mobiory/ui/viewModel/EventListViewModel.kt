package com.example.mobiory.ui.viewModel

import androidx.lifecycle.ViewModel
import com.example.mobiory.data.model.Event
import com.example.mobiory.data.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class EventListViewModel @Inject constructor (private val eventRepository: EventRepository) : ViewModel() {

    val eventList get() =  eventRepository.getAllEventsFlow()

    fun getSearchedEvents(searchString: String): Flow<List<Event>> {
        return eventRepository.getSearchedEvents(searchString)
    }
}