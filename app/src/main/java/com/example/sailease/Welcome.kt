package com.example.sailease

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun Welcome(navController: NavController) {
    Text(text = "Welcome")
    Button(onClick = { navController.navigate(route = Screen.Login.route)}) {
        Text(text = "Login")
    }
}