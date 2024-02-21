package com.example.sailease

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun Login(navController: NavController) {
    Button(onClick = { navController.navigate(route = Screen.Home.route)}) {
        Text(text = "Home")
    }

}