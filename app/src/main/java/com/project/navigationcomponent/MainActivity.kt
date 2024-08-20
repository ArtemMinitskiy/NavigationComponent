package com.project.navigationcomponent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.project.navigationcomponent.Constants.BACK_MESSAGE
import com.project.navigationcomponent.Constants.LIST_ITEM_NAME
import com.project.navigationcomponent.ui.theme.NavigationComponentTheme
import com.project.navigationcomponent.ui.theme.Purple40

class MainActivity : ComponentActivity() {
    private val items = listOf(NavigationItem.Navigation.Screen1, NavigationItem.Navigation.Screen2)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val showBottomBarState = rememberSaveable { (mutableStateOf(false)) }

            //The NavController is the central API for the Navigation component.
            //It is stateful and keeps track of the back stack of composables that make up the screens in your app and the state of each screen.
            val navController = rememberNavController()
            val rootNavBackStackEntry by navController.currentBackStackEntryAsState()
            when (rootNavBackStackEntry?.destination?.route) {
                NavigationItem.List.route -> {
                    showBottomBarState.value = false
                }
                NavigationItem.Description.route -> {
                    showBottomBarState.value = false
                }
                NavigationItem.Navigation.Screen1.route -> {
                    showBottomBarState.value = true
                }
                NavigationItem.Navigation.Screen2.route -> {
                    showBottomBarState.value = true
                }
            }

            NavigationComponentTheme {
                Scaffold(
                    bottomBar = {
                        if (showBottomBarState.value) {
                            NavigationBar {
                                val currentDestination = rootNavBackStackEntry?.destination
                                items.forEach { screen ->
                                    NavigationBarItem(
                                        icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
                                        label = { Text(screen.name) },
                                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                        onClick = {
                                            navController.navigate(screen.route) {
                                                // Pop up to the start destination of the graph to
                                                // avoid building up a large stack of destinations
                                                // on the back stack as users select items
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                // Avoid multiple copies of the same destination when
                                                // reselecting the same item
                                                launchSingleTop = true
                                                // Restore state when reselecting a previously selected item
                                                restoreState = true
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    //Each NavController must be associated with a single NavHost composable.
                    //The NavHost links the NavController with a navigation graph that specifies the composable destinations that you should be able to navigate between.
                    //As you navigate between composables, the content of the NavHost is automatically recomposed.
                    //Each composable destination in your navigation graph is associated with a route.
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                    ) {
                        NavHost(navController = navController, startDestination = NavigationItem.List.route) {
                            mainGraph(navController)
                            navigationGraph(navController)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ListScreen(navController: NavHostController) {
    //Retrieve data after navigating back from SecondScreen with the help of savedStateHandle of current back-stack entry of NavController .
    val msg = navController.currentBackStackEntry?.savedStateHandle?.get<String>(BACK_MESSAGE)
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            msg?.let {
                Text(modifier = Modifier.fillMaxWidth(), text = it, style = TextStyle(textAlign = TextAlign.Center))
            }
            Spacer(modifier = Modifier.padding(16.dp))
            Button(modifier = Modifier, onClick = {
//                navController.navigate(NavigationItem.Description.route)
                //With optional navigation arguments
                navController.navigate("${NavigationItem.Description.route}?$LIST_ITEM_NAME=15")
            }) {
                Text(text = "Go to Description")
            }
            Spacer(modifier = Modifier.padding(16.dp))
            Button(modifier = Modifier, onClick = {
                //With optional navigation arguments
                navController.navigate(NavigationItem.Navigation.Screen1.route)
            }) {
                Text(text = "Go to nested graph")
            }
        }

    }
}

@Composable
fun DescriptionScreen(navController: NavHostController, listItemName: String = "") {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Purple40)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(modifier = Modifier.fillMaxWidth(), text = listItemName, style = TextStyle(textAlign = TextAlign.Center))
            Spacer(modifier = Modifier.padding(16.dp))
            Button(modifier = Modifier, onClick = {
                navController.popBackStack()
            }) {
                Text(text = "Go to Back")
            }
            Spacer(modifier = Modifier.padding(16.dp))
            //Navigating back with Result
            //Put data inside savedStateHandle of previous back-stack entry of NavController.
            Button(modifier = Modifier, onClick = {
                navController.previousBackStackEntry?.savedStateHandle?.set(BACK_MESSAGE, BACK_MESSAGE)
                navController.popBackStack()
            }) {
                Text(text = "Go to Back with Data")
            }
        }
    }
}

@Composable
fun Screen1(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth(), text = "Screen1", style = TextStyle(textAlign = TextAlign.Center))
    }
}

@Composable
fun Screen2(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth(), text = "Screen2", style = TextStyle(textAlign = TextAlign.Center))
    }
}