package com.example.mobiory.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mobiory.data.model.Event
import com.example.mobiory.ui.viewModel.EventListViewModel

@Composable
fun EventListScreen() {
    val eventListViewModel = hiltViewModel<EventListViewModel>()
    //var isLoading by remember { mutableStateOf(true) }
    val eventsListInitial by eventListViewModel.eventList.collectAsState(initial = emptyList())

    val (events, setEvents) = remember { mutableStateOf(eventsListInitial) }

    val tabTitles = listOf("All", "Favorites")
    var tabIndex by remember { mutableStateOf(0) }

    LaunchedEffect(eventsListInitial) {
        setEvents(eventsListInitial)
        //isLoading = false
    }

    LaunchedEffect(tabIndex) {
        if(tabIndex == 0) {
            setEvents(eventsListInitial)
        } else {
            eventListViewModel.getFavoriteEvents().collect {
                setEvents(it)
            }
        }
    }
    Scaffold(
        topBar = {
            TabRow(selectedTabIndex = tabIndex) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title) },
                        selected = tabIndex == index,
                        onClick = { tabIndex = index }
                    )
                }
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                //verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (tabIndex == 0) {
                    SearchBar(eventListViewModel, setEvents)
                }
                //if (isLoading)
                //    CircularProgressIndicator(modifier = Modifier.width(64.dp))
                //else {
                EventList(eventListViewModel, events)
                //}
            }
        }
    )
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
                            setExpandedDialog(false)
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