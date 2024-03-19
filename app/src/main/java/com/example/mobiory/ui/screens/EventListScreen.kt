package com.example.mobiory.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
//import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mobiory.data.model.Event
import com.example.mobiory.ui.viewModel.EventListViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Add
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Space
import androidx.compose.material.icons.filled.Article

@Composable
fun EventListScreen() {
    val eventListViewModel = hiltViewModel<EventListViewModel>()
    //var isLoading by remember { mutableStateOf(true) }
    val eventsListInitial by eventListViewModel.eventList.collectAsState(initial = emptyList())

    val (events, setEvents) = remember { mutableStateOf(eventsListInitial) }

    LaunchedEffect(eventsListInitial) {
        setEvents(eventsListInitial)
        //isLoading = false
    }

    Column {
        SearchBar(eventListViewModel, setEvents)
        //if (isLoading)
        //    CircularProgressIndicator(modifier = Modifier.width(64.dp))
        //else {
        EventList(eventListViewModel, events)
        //}
    }

}

@Composable
fun EventItem(eventListViewModel: EventListViewModel, event: Event) {

    var expanded by remember { mutableStateOf(false) }
    var onFavoriteClick by remember { mutableStateOf(false) }

    LaunchedEffect(onFavoriteClick) {
        if (onFavoriteClick) {
            eventListViewModel.toggleFavorite(event.id)
            onFavoriteClick = false
        }
    }

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
                    text = event.label?.labelEN ?: event.label?.labelFR ?: "no label",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f)
                )
                +
                Row() {
                    IconButton(
                        onClick = {
                            onFavoriteClick = true
                        }) {
                        Icon(
                            imageVector = if (event.favorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite"
                        )
                    }
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
                            text = event.description?.descriptionEN
                                ?: event.description?.descriptionEN
                                ?: "",
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
                    event.country?.let {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Country"
                            )
                            Text(
                                text = event.country,
                                fontSize = 15.sp,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EventList(eventListViewModel: EventListViewModel, events: List<Event>) {

    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(events) { event ->
            EventItem(eventListViewModel, event = event)
        }
    }
}

fun getDate(date: Date?): String {
    val dateFormat = SimpleDateFormat("dd MM yyyy", Locale.getDefault())
    return if (date != null) dateFormat.format(date).toString()
    else ""
}

@Composable
fun TagDialog(
    setExpandedDialog: (Boolean) -> Unit,
    setUpdateTagClick: (Boolean) -> Unit,
    setNewTag: (String) -> Unit,
    tag: String
) {
    var localTag by remember { mutableStateOf(tag) }
    Dialog(onDismissRequest = {
        setExpandedDialog(false)
    }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = localTag,
                    onValueChange = { localTag = it },
                    label = { Text("Tag") },
                    modifier = Modifier.width(250.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { setExpandedDialog(false) },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Dismiss")
                    }
                    TextButton(
                        onClick = {
                            setNewTag(localTag)
                            setUpdateTagClick(true)
                        },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}

@Composable
fun Tag(tag: String) {
    Box(
        modifier = Modifier
            .height(22.dp)
            .padding(5.dp, 0.dp)
            .background(
                shape = RoundedCornerShape(25.dp),
                color = androidx.compose.ui.graphics.Color.Blue
            ),
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(
            modifier = Modifier.padding(15.dp, 0.dp),
            text = tag,
            style = TextStyle(
                color = androidx.compose.ui.graphics.Color.White,
            )
        )
    }
}

