package com.example.mobiory.ui.screens

import android.content.Context
import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.mobiory.data.model.Event
import com.example.mobiory.ui.viewModel.EventListViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.flow.firstOrNull
import java.util.Date

@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun HomeScreen(navigator : NavHostController , context : Context, setHomeNotifications: (Boolean) -> Unit) {
    val (notificationPermissions, setNotificationPermissions) = remember {
        mutableStateOf(false)
    }
    //To test on different device
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val permissionState = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
        if (!permissionState.status.isGranted) {
            Button(onClick = { permissionState.launchPermissionRequest() }) {
                Text(text = "Allow Notifications")
            }
        } else {
            setNotificationPermissions(true)
        }
    } else {
        setNotificationPermissions(true)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            EventOfTheDay(navigator ,context, notificationPermissions, setHomeNotifications)
        }
        item {
            // Add event proches //TODO
        }
    }

}

@Composable
fun EventOfTheDay(navigator : NavHostController, context: Context, notificationPermissions: Boolean, setHomeNotifications: (Boolean) -> Unit) {
    val eventListViewModel = hiltViewModel<EventListViewModel>()
    val today = remember { Date() }
    val (event, setEvent) = remember(today) {
        mutableStateOf<Event?>(null)
    }

    //To test
    LaunchedEffect(today) {
        var eventToday = eventListViewModel.getRandomEventForToday(today).firstOrNull()
        if (eventToday == null) {
            eventToday = eventListViewModel.getRandomEventForMonth(today).firstOrNull()
        }
        if (notificationPermissions)
            eventToday?.let {
                eventListViewModel.showSimpleNotification(it)
                setHomeNotifications(true)
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
            style = TextStyle(
                textDecoration = TextDecoration.Underline,
                fontWeight = FontWeight.Bold, fontSize = 24.sp
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        if (event != null) {
            EventItem(eventListViewModel = eventListViewModel, event = event, alwaysExpanded = true  , navigator,context)
        }
    }
}