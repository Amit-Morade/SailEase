package com.example.sailease

sealed class Screen (val route: String) {
    object Welcome: Screen(route = "screen_one")
    object Home: Screen(route = "screen_two")
    object Login: Screen(route = "screen_three")
}