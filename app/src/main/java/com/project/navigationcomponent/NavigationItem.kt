package com.project.navigationcomponent

sealed class NavigationItem(val route: String, val name: String = "") {
    object List : NavigationItem(Screen.LIST.name)
    object Description : NavigationItem(Screen.DESCRIPTION.name)

    object Navigation : NavigationItem(Screen.MAIN.name, "MAIN") {
        object Screen1 : NavigationItem(Screen.SCREEN_1.name, "Screen1")
        object Screen2 : NavigationItem(Screen.SCREEN_2.name, "Screen2")
    }
}