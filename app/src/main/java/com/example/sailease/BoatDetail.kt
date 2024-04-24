package com.example.sailease

import android.util.Log
import android.widget.RatingBar
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import java.util.UUID

import com.example.sailease.BoatList
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

@Composable
fun BoatDetail(boatId: String?,  navController: NavHostController) {
    var yourRatingValue by remember { mutableStateOf(0f) }
    suspend fun findBoatById(id: String?): Boat? {
        val db = Firebase.firestore

        // Reference to the "boats" collection
        val boatsCollection = db.collection("boats")

        // Retrieve the document with the given ID
        val boatDocument = boatsCollection.document(boatId.toString())

        return try {
            val documentSnapshot = boatDocument.get().await()
            documentSnapshot.toObject(Boat::class.java)
        } catch (e: Exception) {
            Log.e("findBoatById", "Error fetching boat details", e)
            null
        }
    }
    Log.i("BoatDetail", "avc")
    var boat by remember { mutableStateOf<Boat?>(null) }

    var showFullDescription by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) } // State variable for dialog visibility
    @Composable
    fun RatingBar(
        rating: Float,
        maxRating: Int,
        onRatingChanged: (Float) -> Unit
    ) {
        Row {
            for (i in 1..maxRating) {
                Icon(
                    imageVector = if (i <= rating) Icons.Default.Star else Icons.Default.Star,
                    contentDescription = null,
                    tint = if (i <= rating) Color(0xFFE5A400) else Color.Gray,
                    modifier = Modifier
                        .clickable { onRatingChanged(i.toFloat()) }
                        .padding(4.dp)
                )
            }
        }
    }
    LaunchedEffect(boatId) {
        Log.i("checking", "updated")
        boat = findBoatById(boatId)
        Log.i("check", boat.toString())
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column (
            modifier = Modifier
                .padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 80.dp)
                .fillMaxWidth()
                .verticalScroll(enabled = true, state = rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,

        ) {
            boat?.let {myBoat ->
                Image(painter = painterResource(id = R.drawable.sailing_yacht), contentDescription = "Boat")
                Spacer(modifier = Modifier.height(10.dp)) // Add some spacing
                Text(
                    text = myBoat.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Price: ${myBoat.price}",
                    fontSize = 18.sp,
                    color = Color.Gray
                )
                Text(
                    text = "Availability: ${myBoat.availability}",
                    fontSize = 18.sp,
                    color = Color.Gray
                )


                val singapore = LatLng(myBoat.latitude, myBoat.longitude)
                val cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(singapore, 10f)
                }
                Spacer(modifier = Modifier.height(10.dp)) // Add some spacing
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState
                ) {
                    Marker(
                        state = MarkerState(position = singapore),
                        title = "Singapore",
                        snippet = "Marker in Singapore"
                    )
                }
                Spacer(modifier = Modifier.height(10.dp)) // Add some spacing
                if (showFullDescription) {
                    Text(
                        text = myBoat.description,
                        fontSize = 18.sp
                    )
                } else {
                    Text(
                        text = "${myBoat.description.take(150)}...", // Show only first 100 characters
                        fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.height(10.dp)) // Add some spacing
                Text(
                    text = if (showFullDescription) "Show Less" else "Learn More",
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { showFullDescription = !showFullDescription }
                )

                if(!myBoat.rented) {
                    Button(
                        onClick = {
                            val currentUser = FirebaseAuth.getInstance().currentUser
                            val renterId = currentUser?.uid

                            val rentalData = hashMapOf(
                                "boatId" to boatId,
                                "renterID" to renterId,
                            )
                            val db = Firebase.firestore
                            db.collection("rental")
                                .add(rentalData)
                                .addOnSuccessListener { documentReference ->
                                    if (boatId != null) {
                                        db.collection("boats")
                                            .document(boatId)
                                            .update("rented", true)
                                            .addOnSuccessListener {
                                                // Rental document added and rented field updated successfully
                                                Log.d("RentNow", "Rental document added with ID: ${documentReference.id}, rented field updated")
                                                // Show success dialog or perform other actions if needed
                                            }
                                            .addOnFailureListener { e ->
                                                // Error updating rented field
                                                Log.e("RentNow", "Error updating rented field in Boat document", e)
                                                // Show error dialog or handle failure case if needed
                                            }
                                    }
                                }
                                .addOnFailureListener { e ->
                                    Log.e("rentalfailure", "Error adding rental document", e)
                                    // Show error dialog or handle failure case if needed
                                }
                            showDialog = true
                            navController.navigate("User")},
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Rent Now")
                    }
                }
                else {
                    Surface(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = Color.Transparent
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 8.dp),
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Your Rating: ",
                                color = LocalContentColor.current
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            RatingBar(
                                rating = yourRatingValue,
                                maxRating = 5,
                                onRatingChanged = { newRating ->
                                    yourRatingValue = newRating // Update your rating value
                                }
                            )
                        }
                    }
                }

                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text(text = "Boat is Rented") },
                        text = { Text(text = "Thank you for renting this boat!") },
                        confirmButton = {
                            Button(onClick = { showDialog = false }) {
                                Text(text = "OK")
                            }
                        }
                    )
                }
 
        } ?: Text(
                text = "Searching...",
                fontSize = 18.sp,
                color = Color.Black

            )
        }
    }

}

