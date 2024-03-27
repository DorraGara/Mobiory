package com.example.mobiory

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ViewList
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mobiory.ui.screens.ArticleScreen
import com.example.mobiory.ui.screens.EventListScreen
import com.example.mobiory.ui.screens.HomeScreen
import com.example.mobiory.ui.screens.SettingsScreen
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
@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)


        setContent {
            MobioryTheme {
                val context = LocalContext.current
                val (homeNotifications, setHomeNotifications) = remember { mutableStateOf(false) }
                val items = listOf(
                    BottomNavigationItem(
                        title = "Home",
                        selectedIcon = Icons.Filled.Home,
                        unselectedIcon = Icons.Outlined.Home,
                        hasNews = homeNotifications,
                    ),
                    BottomNavigationItem(
                        title = "Event list",
                        selectedIcon = Icons.Filled.ViewList,
                        unselectedIcon = Icons.Outlined.ViewList,
                        hasNews = false,
                    ),
                    //add Quiz and Frise chronologique here
                    BottomNavigationItem(
                        title = "Settings",
                        selectedIcon = Icons.Filled.Settings,
                        unselectedIcon = Icons.Outlined.Settings,
                        hasNews = false,
                    ),
                )
                var selectedItemIndex by rememberSaveable {
                    mutableIntStateOf(0)
                }

                val navController = rememberNavController()


                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        bottomBar = {
                            NavigationBar {
                                items.forEachIndexed { index, item ->
                                    NavigationBarItem(
                                        selected = selectedItemIndex == index,
                                        onClick = {
                                            selectedItemIndex = index
                                            navController.navigate(item.title)
                                            if (selectedItemIndex == 0)
                                                setHomeNotifications(false)
                                        },
                                        label = {
                                            Text(text = item.title)
                                        },
                                        alwaysShowLabel = false,
                                        icon = {
                                            BadgedBox(
                                                badge = {
                                                    if (item.hasNews) {
                                                        Badge()
                                                    }
                                                }
                                            ) {
                                                Icon(
                                                    imageVector = if (index == selectedItemIndex) {
                                                        item.selectedIcon
                                                    } else item.unselectedIcon,
                                                    contentDescription = item.title
                                                )
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    ) { innerPadding ->
                        Column(
                            modifier = Modifier
                                .padding(innerPadding),
                        ) {
                            NavHost(navController = navController, startDestination = "home") {
                                composable(Routes.Home.route) { HomeScreen(navController, context, setHomeNotifications) }
                                composable(Routes.Event.route) { EventListScreen(navController,context) }
                                //add rest of screen composable here (Quiz and frise)
                                composable(Routes.Settings.route) { SettingsScreen() }
                                composable(Routes.Article.route) { ArticleScreen() }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
}

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean,
)


sealed class Routes(val route: String) {
    data object Home : Routes("Home")
    data object Event : Routes("Event List")
    data object Settings : Routes("Settings")
    data object Article : Routes("Article")
}
