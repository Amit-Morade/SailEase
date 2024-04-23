package com.example.sailease

sealed class Screen (val route: String) {



    object Welcome: Screen(route = "screen_one")
    object Home: Screen(route = "screen_two")
    object Login: Screen(route = "screen_three")
    object Sign: Screen(route = "screen_four")
    object Settings: Screen(route = "screen_five")

    object User: Screen(route = "screen_six")

    object Rent: Screen(route = "screen_seven")

    object ManageBoats: Screen(route = "screen_eight")

    object Location: Screen(route = "screen_nine")
//    object Rent: Screen(route = "screen_six")
}