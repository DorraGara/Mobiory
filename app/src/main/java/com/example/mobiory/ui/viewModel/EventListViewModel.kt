package com.example.mobiory.ui.viewModel

import android.annotation.SuppressLint
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import com.example.mobiory.data.model.Event
import com.example.mobiory.data.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class EventListViewModel @Inject constructor (
    private val eventRepository: EventRepository,
    private val notificationBuilder: NotificationCompat.Builder,
    private val notificationManager: NotificationManagerCompat
) : ViewModel() {

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
    suspend fun toggleFavorite(eventId: Int) {
        eventRepository.toggleFavorite(eventId)
    }
    suspend fun updateTag(eventId: Int, label: String) {
        eventRepository.updateTag(eventId, label)
    }
    fun getFavoriteEvents(): Flow<List<Event>> {
        return eventRepository.getFavoriteEvents()
    }

    fun getRandomEventForToday(today: Date): Flow<Event?> {
        return eventRepository.getRandomEventForToday(today)
    }
    fun getRandomEventForMonth(today: Date): Flow<Event?> {
        return eventRepository.getRandomEventForMonth(today)
    }
    @SuppressLint("MissingPermission")
    fun showSimpleNotification(event:Event) {
        notificationManager.notify(1, notificationBuilder
            .setContentTitle("Event of the day")
            .setContentText(event.label?.labelEN)
            .build())
    }
}