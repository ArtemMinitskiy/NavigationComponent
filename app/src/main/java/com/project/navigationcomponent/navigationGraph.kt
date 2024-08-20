package com.project.navigationcomponent

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation

fun NavGraphBuilder.navigationGraph(
    navController: NavHostController
) {
    navigation(
        startDestination = NavigationItem.Navigation.Screen1.route,
        route = NavigationItem.Navigation.route
    ) {
        composable(
            route = NavigationItem.Navigation.Screen1.route,
        ) {
            Screen1(navController)
        }
        composable(
            route = NavigationItem.Navigation.Screen2.route,
        ) {
            Screen2(navController)
        }
    }
}