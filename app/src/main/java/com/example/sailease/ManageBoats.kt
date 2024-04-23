package com.example.sailease

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@Composable
fun BoatManagementScreen(navController: NavController) {
    // Initialize an empty list of boats
    var boats by remember { mutableStateOf<List<Boat>>(emptyList()) }

    // Fetch boats owned by the current user
    LaunchedEffect(key1 = Unit) {
        val fetchedBoats = getBoatsForCurrentUser()
        boats = fetchedBoats
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Row {
            Text(
                text = "Your Boats",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Spacer(modifier = Modifier.width(160.dp))

        }

        Divider(color = Color.LightGray)
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(boats) { boat ->
                BoatItem(boat = boat, onItemClick = {
                    navController.navigate("boatDetail/${boat.boatId}")
                }, showRented = true)
                Divider(color = Color.LightGray)

            }
        }

        Button(
            onClick = {
                navController.navigate("addBoat")
            },
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 8.dp, bottom = 90.dp)
        ) {
            Text("Add Boat")
        }
    }
}

suspend fun getBoatsForCurrentUser(): List<Boat> {
    // Get current user ID
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid ?: return emptyList() // Return empty list if user is not authenticated

    // Initialize Firestore
    val db = FirebaseFirestore.getInstance()

    // Query boats collection for documents where ownerId matches current user's ID
    val boatsQuery = db.collection("boats")
        .whereEqualTo("ownerId", userId)
        .get()
        .await()

    // Convert query results to list of Boat objects
    val boats = mutableListOf<Boat>()
    for (document in boatsQuery.documents) {
        val boat = document.toObject(Boat::class.java)
        boat?.let { boats.add(it) }
    }

    return boats
}