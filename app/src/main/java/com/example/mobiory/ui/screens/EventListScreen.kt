package com.example.mobiory.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.unit.DpOffset
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
    val eventListViewModel = hiltViewModel<EventListViewModel>()
    val eventsListInitial by eventListViewModel.eventList.collectAsState(initial = emptyList())

    val (events,setEvents) = remember { mutableStateOf(eventsListInitial) }

    LaunchedEffect(eventsListInitial) {
         setEvents(eventsListInitial)
    }

    Column {
        SearchBar(eventListViewModel,setEvents)
        EventList(events)
    }

}

@Composable
fun EventItem(event: Event) {

    var expanded by remember { mutableStateOf(false) }

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
                    text = event.label?.labelEN ?:  event.label?.labelFR ?: "no label",
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
                        text = event.description?.descriptionEN ?: event.description?.descriptionEN ?: "",
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

@Composable
fun EventList(events: List<Event>) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
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

@Composable
fun SearchBar(
    eventListViewModel: EventListViewModel,
    setEvents: (List<Event>) -> Unit
    ) {
    val (searchString,setSearchString) = remember { mutableStateOf("") }
    val (searchClicked, setSearchClicked) = remember { mutableStateOf(false) }
    val (filterOption,setfilterOption) = remember { mutableStateOf(FilterOption.OPTION_1) }
    val (filterClicked, setFilterClicked) = remember { mutableStateOf(false) }
    val (sortOption,setSortOption) = remember { mutableStateOf(SortOption.OPTION_A) }

    val (sortClicked, setSortClicked) = remember { mutableStateOf(false) }


    LaunchedEffect(searchClicked,searchString) {
        if (searchClicked) {
            eventListViewModel.getSearchedEvents(searchString).collect {
                setEvents(it)
                setSearchClicked(false)
            }
        }
    }

    var expandedFilter by remember { mutableStateOf(false) }
    var expandedSort by remember { mutableStateOf(false) }

    Column {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchString,
                onValueChange = { setSearchString(it) },
                label = { Text("Search") },
                modifier = Modifier.width(250.dp)
                //    .weight(1f)
                //    .padding(end = 8.dp)
            )

            IconButton(onClick = { setSearchClicked(true)}) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }

            IconButton(onClick = { expandedFilter = true }) {
                Icon(Icons.Default.FilterAlt, contentDescription = "Filter")
            }

            IconButton(onClick = { expandedSort = true }) {
                Icon(Icons.Default.Sort, contentDescription = "Sort")
            }
        }
        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopEnd
        ) {
            // Filter menu
            DropdownMenu(
                expanded = expandedFilter,
                onDismissRequest = { expandedFilter = false },
                modifier = Modifier
                    .width(maxWidth/2),
                offset = DpOffset(maxWidth /2,0.dp),

            ) {
                FilterOption.entries.forEach { option ->
                    DropdownMenuItem(text = { Text(option.label) }, onClick = {
                        setfilterOption(option)
                        setFilterClicked(true)
                        expandedFilter = false
                    })
                }
            }

            // Sort menu
            DropdownMenu(
                expanded = expandedSort,
                onDismissRequest = { expandedSort = false },
                modifier = Modifier
                    .width(maxWidth/2),
                offset = DpOffset(maxWidth /2,0.dp)

            ) {
                SortOption.entries.forEach { option ->
                    DropdownMenuItem(text = { Text(option.label) }, onClick = {
                        setSortOption(option)
                        setSortClicked(true)
                        expandedSort = false
                    })
                }
            }
        }
    }
}

enum class FilterOption(val label: String) {
    OPTION_1("Option 1"),
    OPTION_2("Option 2"),
    OPTION_3("Option 3")
}

enum class SortOption(val label: String) {
    OPTION_A("Option A"),
    OPTION_B("Option B"),
    OPTION_C("Option C")
}

