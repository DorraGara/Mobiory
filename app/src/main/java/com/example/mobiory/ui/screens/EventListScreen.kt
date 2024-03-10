package com.example.mobiory.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mobiory.data.model.Event
import com.example.mobiory.ui.viewModel.EventListViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun EventListScreen() {
    EventList()
}
@Composable
fun EventItem(event: Event) {

    var expanded by remember { mutableStateOf (false) }

    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                expanded = !expanded
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                modifier = Modifier.fillMaxWidth()

            ) {
                Text(
                    text = event.label?.labelEN ?: "no label",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f)
                )
                IconButton(
                    onClick = {
                        expanded = !expanded
                    }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Drop-Down Arrow"
                    )
                }
            }
            event.popularity?.let { popularity ->
                val progress = 1 - (popularity.popularityEN.toFloat() / 7000000f)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "URL"
                    )
                    LinearProgressIndicator(
                        progress = progress.coerceIn(0f, 1f),
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }

            }
            if (expanded) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.List,
                        contentDescription = "URL"
                    )
                    Text(
                        text = event.description?.descriptionEN?:"",
                        fontSize = 15.sp,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "URL"
                        )
                        val dateShown = event.pointInTime ?: event.startDate ?: event.endDate
                        Text(
                            text = getDate(dateShown),
                            fontSize = 15.sp,
                            modifier = Modifier.padding(8.dp)
                        )
                    }

            }
        }
    }
}

@Composable
fun EventList() {
    val eventListViewModel = hiltViewModel<EventListViewModel>()
    val events by eventListViewModel.eventList.collectAsState(initial = emptyList())

    LazyColumn(contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(events) { event ->
            EventItem(event = event)
        }
    }
}

fun getDate(date: Date?): String {
    val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    return if (date != null) dateFormat.format(date).toString()
    else ""
}
