package com.example.mobiory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.mobiory.data.AppDatabase
import com.example.mobiory.ui.screens.EventListScreen
import com.example.mobiory.ui.theme.MobioryTheme
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
    //val (isLoading,setIsLoading) = remember { mutableStateOf(false) }

    Column {
        Button(onClick = {
            //setIsLoading(true)
            AppDatabase.updatetDatabase(context)
            //setIsLoading(false)
        }) {
            Text("Refresh database")
        }
        //if (isLoading)
        //    CircularProgressIndicator(modifier = Modifier.width(64.dp))
        //else {
            EventListScreen()
        //}
    }
}
