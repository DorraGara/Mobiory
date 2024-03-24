package com.example.mobiory.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mobiory.data.model.Event
import com.example.mobiory.ui.viewModel.EventListViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import java.util.Date

@Composable
fun HomeScreen() {
    Column {

        EventOfTheDay()

    }

}

@Composable
fun EventOfTheDay() {
    val eventListViewModel = hiltViewModel<EventListViewModel>()
    val today = remember { Date() }
    val (event, setEvent) = remember(today) {
        mutableStateOf<Event?>(null)
    }

    LaunchedEffect(true) {
        var eventToday = eventListViewModel.getRandomEventForToday(today).firstOrNull()
        if (eventToday == null) {
            eventToday = eventListViewModel.getRandomEventForMonth(today).firstOrNull()
        }
        setEvent(eventToday)
    }

    if (event != null) {
        EventItem(eventListViewModel = eventListViewModel, event = event)
    }
}