package com.project.navigationcomponent

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.project.navigationcomponent.Constants.LIST_ITEM_NAME

fun NavGraphBuilder.mainGraph(
    navController: NavHostController
) {
    composable(NavigationItem.List.route) {
        ListScreen(navController)
    }
    composable(
//                        "${NavigationItem.Description.route}/{$LIST_ITEM_NAME}",
        "${NavigationItem.Description.route}?$LIST_ITEM_NAME={$LIST_ITEM_NAME}",
        //Navigation Compose also supports optional navigation arguments. Optional arguments differ from required arguments in two ways:
        //
        //They must be included using query parameter syntax ("?argName={argName}")
        //They must have a defaultValue set, or have nullable = true (which implicitly sets the default value to null)
        //Now, even if there is no argument passed to the destination, the defaultValue = "user1234", is used instead.
        arguments = listOf(navArgument(LIST_ITEM_NAME) {
            type = NavType.StringType
            defaultValue = "user1234"
        })
    ) { backStackEntry ->
        DescriptionScreen(navController, backStackEntry.arguments?.getString(LIST_ITEM_NAME).toString())
    }
}