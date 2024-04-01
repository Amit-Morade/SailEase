package com.example.sailease

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Settings(navController: NavController, viewModel: SignInViewModel) {
    val currentuser = remember { mutableStateOf(FirebaseAuth.getInstance().currentUser) }
    if(currentuser.value != null) {
        Text(text = currentuser.value?.email + "")
    }else {
        Text(text = "null")
    }

    Button(onClick = {
        viewModel.onSignOut()
        navController.navigate(route = Screen.Login.route)
    } ) {
        Text(text = "SignOut")

    }
}