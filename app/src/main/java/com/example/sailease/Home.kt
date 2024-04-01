package com.example.sailease

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.example.sailease.model.AccountService
import com.example.sailease.model.AccountServiceImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun Home(navController:NavController, viewModel: SignInViewModel) {
    val currentuser = remember { mutableStateOf(FirebaseAuth.getInstance().currentUser) }
    val accountService: AccountService = AccountServiceImpl()

    if(currentuser.value == null) {
        navController.navigate(Screen.Login.route)
    }

    LaunchedEffect(Unit) {
        val auth = FirebaseAuth.getInstance()
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            currentuser.value = auth.currentUser
        }
        auth.addAuthStateListener(authStateListener)

    }

//    if(currentuser.value != null) {
//        Text(text = currentuser.value?.email + "")
//    }else {
//        Text(text = "null")
//    }


//
//    Button(onClick = {
//        viewModel.onSignOut()
//        navController.navigate(route = Screen.Login.route)
//    } ) {
//        Text(text = "SignOut")
//
//    }

    BoatList(boats = sampleBoats)
    
}

val sampleBoats = listOf(
    Boat(
        name = "Sailing Yacht",
//        imageResId = R.drawable.sailing_yacht,
        price = "$500",
        availability = "Available"
    ),
    Boat(
        name = "Speedboat",
//        imageResId = R.drawable.speedboat,
        price = "$300",
        availability = "Unavailable"
    ),
    Boat(
        name = "Fishing Boat",
//        imageResId = R.drawable.fishing_boat,
        price = "$400",
        availability = "Available"
    ),
    Boat(
        name = "Pontoon Boat",
//        imageResId = R.drawable.pontoon_boat,
        price = "$250",
        availability = "Available"
    ),
    Boat(
        name = "Kayak",
//        imageResId = R.drawable.kayak,
        price = "$50",
        availability = "Available"
    ),
    Boat(
        name = "Sailing Yacht",
//        imageResId = R.drawable.sailing_yacht,
        price = "$500",
        availability = "Available"
    ),
    Boat(
        name = "Speedboat",
//        imageResId = R.drawable.speedboat,
        price = "$300",
        availability = "Unavailable"
    ),
    Boat(
        name = "Fishing Boat",
//        imageResId = R.drawable.fishing_boat,
        price = "$400",
        availability = "Available"
    ),
    Boat(
        name = "Pontoon Boat",
//        imageResId = R.drawable.pontoon_boat,
        price = "$250",
        availability = "Available"
    ),
    Boat(
        name = "Kayak",
//        imageResId = R.drawable.kayak,
        price = "$50",
        availability = "Available"
    )

)
