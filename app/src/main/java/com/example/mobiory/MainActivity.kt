package com.example.mobiory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mobiory.data.AppDatabase
import com.example.mobiory.ui.theme.MobioryTheme
import com.example.mobiory.ui.viewModel.EventListViewModel
import dagger.hilt.android.AndroidEntryPoint


@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MobioryTheme {
        MainScreen()
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MobioryTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current

    Column {
        Button(onClick = {
            AppDatabase.updatetDatabase(context)
        }) {
            Text("Import Events")
        }

        EventList()
    }
}

@Composable
fun EventList() {
    val eventListViewModel = hiltViewModel<EventListViewModel>()
    val events by eventListViewModel.eventList.collectAsState(initial = emptyList())

    LazyColumn(contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(events) { event ->
            EventItem(id = event.id, label = event.label)
        }
    }
}

@Composable
fun EventItem(id : Int = 0, label : String = "") {
    Row(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .background(Color.LightGray)) {
        Text(modifier = Modifier.padding(8.dp), text = id.toString())
        Text(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(), text = label )
    }
}