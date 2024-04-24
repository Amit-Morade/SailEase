package com.example.sailease

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.android.gms.tasks.Tasks
//import com.example.sailease.boatz
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore


@SuppressLint("RememberReturnType")
@Composable
fun User(navController: NavHostController) {
    val rentedBoats = remember { mutableStateListOf<Boat>() }
    var loading = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid
        if (userId != null) {
            fetchRentalDataForUser(userId,
                onSuccess = { rentals ->
                    // Update rentedBoats list with fetched rental data
                    rentedBoats.clear()
                    rentedBoats.addAll(rentals)
                    loading.value= false
                },
                onFailure = { e ->
                    // Handle failure case
                    // For example, show an error message
                }
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Boats Rented",
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 10.dp)
        )
        Divider(Modifier.fillMaxWidth().padding(vertical = 16.dp))
        if(loading.value==true) {
            Text(text = "Loading...")
        }else {
            if(rentedBoats.isEmpty()) {
                Text(text = "You have not rented any boat.")
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(rentedBoats) { boat ->
                BoatItem(boat = boat, onItemClick = {
                    navController.navigate("boatDetail/${boat.boatId}")
                }, showRented = false)
            }
        }
        // Add a button to navigate to another screen or perform an action
        Button(
            onClick = { /* TODO: Navigate or perform action */ },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "Next")
        }


    }
}



@Composable
fun RentedBoatItem(boat: Boat, navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = {
                navController.navigate("boatDetail/${boat.boatId}")
            })
            .height(150.dp),
        shape = RoundedCornerShape(8.dp)

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f) // Text column takes most of the space
            ) {
                Text(
                    text = boat.name,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Price: ${boat.price}\nAvailability: ${boat.availability}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            // Image can be added here if available in Boat class
        }
    }
}

fun fetchRentalDataForUser(userId: String, onSuccess: (List<Boat>) -> Unit, onFailure: (Exception) -> Unit) {
    val db = FirebaseFirestore.getInstance()

    db.collection("rental")
        .whereEqualTo("renterID", userId)
        .get()
        .addOnSuccessListener { querySnapshot ->
            val boatIds = mutableListOf<String>()

            // Extract boatIds from rental documents
            for (document in querySnapshot.documents) {
                val boatId = document.getString("boatId")
                boatId?.let { boatIds.add(it) }
            }

            // Fetch boat details for each boatId
            val boats = mutableListOf<Boat>()
            val boatFetchTasks = boatIds.map { boatId ->
                db.collection("boats")
                    .document(boatId)
                    .get()
                    .addOnSuccessListener { boatDocument ->
                        val boat = boatDocument.toObject(Boat::class.java)
                        boat?.let { boats.add(it) }
                    }
            }

            // Wait for all boat fetch tasks to complete
            Tasks.whenAllSuccess<DocumentSnapshot>(boatFetchTasks)
                .addOnSuccessListener {
                    onSuccess(boats)
                }
                .addOnFailureListener { exception ->
                    onFailure(exception)
                }
        }
        .addOnFailureListener { e ->
            onFailure(e)
        }
}

