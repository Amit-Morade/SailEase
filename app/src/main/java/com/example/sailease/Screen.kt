package com.example.sailease

sealed class Screen (val route: String) {


    object Welcome: Screen(route = "screen_one")
    object Home: Screen(route = "screen_two")
    object Login: Screen(route = "screen_three")
    object Sign: Screen(route = "screen_four")
    object Settings: Screen(route = "screen_five")

//    object Rent: Screen(route = "screen_six")
}