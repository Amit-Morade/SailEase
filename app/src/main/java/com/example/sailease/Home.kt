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

    LaunchedEffect(Unit) {
        val auth = FirebaseAuth.getInstance()
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            currentuser.value = auth.currentUser
        }
        auth.addAuthStateListener(authStateListener)
    }

    BoatList(boats = sampleBoats, navController = navController)

}

