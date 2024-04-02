import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.mobiory.data.model.Event
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import com.example.mobiory.ui.viewModel.EventListViewModel

@Composable
fun TimelineScreen(navigator: NavHostController, context: Context) {
    val eventListViewModel = hiltViewModel<EventListViewModel>()
    val eventsListInitial by eventListViewModel.eventList.collectAsState(initial = emptyList())

    val (events, setEvents) = remember { mutableStateOf(eventsListInitial) }

    val tabTitles = listOf("All", "Favorites")
    var tabIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(eventsListInitial) {
        setEvents(eventsListInitial)
    }

    LaunchedEffect(tabIndex) {
        if (tabIndex == 0) {
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
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (tabIndex == 0) {
                    // Display search bar if needed
                }
                EventTimeline(events)
            }
        }
    )
}

@Composable
fun EventTimeline(events: List<Event>) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(events) { event ->
            TimelineEvent(event = event)
        }
    }
}


@Composable
fun TimelineEvent(event: Event) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        BasicTextField(
            value = remember { TextFieldValue(text = event.startDate.toString()) },
            onValueChange = {},
            modifier = Modifier.width(100.dp)
        )
        Column(modifier = Modifier.padding(start = 8.dp)) {
            BasicTextField(
                value = remember { TextFieldValue(text = event.label?.labelEN ?: "") },
                onValueChange = {},
            )
            BasicTextField(
                value = remember { TextFieldValue(text = event.description?.descriptionEN ?: "") },
                onValueChange = {},
            )
        }
    }
}
