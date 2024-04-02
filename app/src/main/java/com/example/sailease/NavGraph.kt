package com.example.sailease

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sailease.model.AccountService
import com.example.sailease.model.AccountServiceImpl

@Composable
fun SetNavGraph(
    navController: NavHostController,
) {
    NavHost(navController = navController, startDestination = Screen.Welcome.route) {
        composable(route = Screen.Welcome.route) {
            Welcome(navController = navController)
        }
        composable(route = Screen.Home.route) {
            Home(navController = navController, viewModel = SignInViewModel(AccountServiceImpl()))
        }
        composable(route = Screen.Login.route) {
            Login(navController = navController, viewModel = SignInViewModel(AccountServiceImpl()))
        }
        composable(route = Screen.Sign.route) {
            Sign(navController = navController, viewModel= SignInViewModel(AccountServiceImpl()))
        }
        composable(route = Screen.Settings.route) {
            Settings(viewModel = SignInViewModel(AccountServiceImpl()), navController = navController)
        }
        composable("boatDetail/{boatId}") { navBackStackEntry ->
            val boatId = navBackStackEntry.arguments?.getString("boatId")
            BoatDetail(boatId)
        }

    }
}