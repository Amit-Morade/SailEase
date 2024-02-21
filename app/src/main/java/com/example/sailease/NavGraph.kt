package com.example.sailease

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun SetNavGraph(
    navController: NavHostController,
) {
    NavHost(navController = navController, startDestination = Screen.Welcome.route) {
        composable(route = Screen.Welcome.route) {
            Welcome(navController = navController)
        }
        composable(route = Screen.Home.route) {
            Home()
        }
        composable(route = Screen.Login.route) {
            Login(navController = navController)
        }
    }
}