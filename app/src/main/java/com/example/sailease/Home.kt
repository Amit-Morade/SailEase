package com.example.sailease

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.example.sailease.model.AccountService
import com.example.sailease.model.AccountServiceImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

@Composable
fun Home(navController:NavController, viewModel: SignInViewModel) {
    val currentuser = remember { mutableStateOf(FirebaseAuth.getInstance().currentUser) }
    val accountService: AccountService = AccountServiceImpl()
    val boats = remember { mutableStateListOf<Boat>() }

    LaunchedEffect(Unit) {
        val boatsCollection = FirebaseFirestore.getInstance().collection("boats")
        boatsCollection.get()
            .addOnSuccessListener { result ->
                for (document in result) {

                    var boat = document.toObject(Boat::class.java)
                    // Now you have a Boat object, you can use it as needed
                    // For example, you can add it to a list of boats
                    boat.boatId = document.id
                    boats.add(boat)
                }
            }
            .addOnFailureListener { exception ->
                // Handle any errors that occur during the process
                Log.e(TAG, "Error getting documents: ", exception)
            }
    }

    DisposableEffect(Unit) {
        val auth = Firebase.auth
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            currentuser.value = auth.currentUser
        }
        auth.addAuthStateListener(authStateListener)
        onDispose {
            auth.removeAuthStateListener(authStateListener)
        }
    }

    BoatList(boats = boats, navController = navController)

}

