package com.example.mobiory.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mobiory.data.model.Event
import com.example.mobiory.ui.viewModel.EventListViewModel
import kotlinx.coroutines.flow.firstOrNull
import java.util.Date

@Composable
fun HomeScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            EventOfTheDay()
        }
        item {
            // Add event proche //TODO
        }
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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Event of the day",
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        if (event != null) {
            EventItem(eventListViewModel = eventListViewModel, event = event, alwaysExpanded = true)
        }

    }
}