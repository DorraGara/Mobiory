package com.example.mobiory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobiory.data.AppContainer
import com.example.mobiory.data.AppDataContainer
import com.example.mobiory.ui.theme.MobioryTheme
import com.example.mobiory.ui.viewModel.EventListViewModel

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MobioryTheme {
        MainScreen()
    }
}

class MainActivity : ComponentActivity() {

    private lateinit var appContainer: AppContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //appContainer = AppDataContainer(applicationContext)
        //val eventListViewModel: EventListViewModel by viewModels()

        setContent {
            MobioryTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //MainScreen(eventListViewModel)
                    MainScreen()
                }
            }
        }
    }
}

@Composable
//fun MainScreen(eventListViewModel : EventListViewModel) {
fun MainScreen() {
    Column {
        Button(onClick = {
        }) {
            Text("Import Events")
        }

        //EventList(eventListViewModel)
    }
}

@Composable
fun EventList(eventListViewModel: EventListViewModel) {

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