package com.example.mobiory.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobiory.data.model.Event
import com.example.mobiory.data.repository.EventRepository
import kotlinx.coroutines.launch
import java.io.File

class EventListViewModel (private val eventRepository: EventRepository) : ViewModel() {

    val eventList get() =  eventRepository.getAllEventsFlow()
    // LiveData for the list of events
    //private val _events: LiveData<List<Event>> = repository.getAllEvents()
    //val events: LiveData<List<Event>>
    //    get() = _events

    // Function to trigger the import of events
    //fun importEventsFromTxt(file: File) {
    //    viewModelScope.launch {
    //        repository.importEventsFromTxt(file)
    //    }
    //}
}