package com.example.sailease

import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun BoatDetail(boatId: String?,  navController: NavHostController) {
    var yourRatingValue by remember { mutableStateOf(0f) }


    var boat by remember { mutableStateOf<Boat?>(null) }
    var averageRating by remember { mutableStateOf<Float?>(0f) }
    val coroutineScope = rememberCoroutineScope()
    var showFullDescription by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) } // State variable for dialog visibility


    LaunchedEffect(Unit) {
        Log.i("checking", "updated")
        boat = findBoatById(boatId)
        coroutineScope.launch {
            if (boatId != null) {
                averageRating =  calculateAverageRating(boatId)
                yourRatingValue = fetchUserRatingForBoat(boatId)
            }
        }
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
                    
                    RatingsList(boatId = boatId.toString())
                }
                else {
                    Surface(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = Color.Transparent
                    ) {

                        Column {
                            Row(
                                modifier = Modifier.padding(vertical = 8.dp),
                                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                            ) {
                                Text(text = "Average Rating: ${averageRating}")

                            }

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
                                    maxRating = 5
                                ) { newRating ->
                                    yourRatingValue = newRating // Update your rating value
                                    val userId = FirebaseAuth.getInstance().currentUser?.uid

                                    coroutineScope.launch() {
                                        updateUserBoatRating(
                                            boatId.toString(),
                                            userId.toString(),
                                            yourRatingValue
                                        )
                                        averageRating = calculateAverageRating(boatId.toString())
                                    }
                                }
                            }

                            

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

suspend fun findBoatById(id: String?): Boat? {
    val db = Firebase.firestore

    // Reference to the "boats" collection
    val boatsCollection = db.collection("boats")

    // Retrieve the document with the given ID
    val boatDocument = boatsCollection.document(id.toString())

    return try {
        val documentSnapshot = boatDocument.get().await()
        documentSnapshot.toObject(Boat::class.java)
    } catch (e: Exception) {
        Log.e("findBoatById", "Error fetching boat details", e)
        null
    }
}

suspend fun updateUserBoatRating(boatId: String, userId: String, newRating: Float) {
    val ratingsCollection = Firebase.firestore.collection("boatRentalRatings")
    val query = ratingsCollection.whereEqualTo("boatId", boatId).whereEqualTo("userId", userId)
    val result = query.get().await()

    if (!result.isEmpty) {

        val ratingDocRef = result.documents[0].reference
        ratingDocRef.update("rating", newRating)
            .addOnSuccessListener {
                // Handle successful update

            }
            .addOnFailureListener { e ->
                // Handle failure

            }
    } else {

        val ratingData = hashMapOf(
            "boatId" to boatId,
            "userId" to userId,
            "rating" to newRating,
            "timestamp" to FieldValue.serverTimestamp()
        )
        ratingsCollection.add(ratingData).await()
    }
}

suspend fun calculateAverageRating(boatId: String): Float {
    val ratingsCollection = Firebase.firestore.collection("boatRentalRatings")
    val query = ratingsCollection.whereEqualTo("boatId", boatId)
    val result = query.get().await()

    var totalRating = 0f
    var ratingCount = 0

    for (document in result) {
        val rating = document.getDouble("rating")?.toFloat() ?: 0f
        totalRating += rating
        ratingCount++
    }

    return if (ratingCount > 0) {
        totalRating / ratingCount
    } else {
        0f
    }
}

suspend fun fetchUserRatingForBoat(boatId: String): Float {
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val ratingsCollection = Firebase.firestore.collection("boatRentalRatings")
    val query = ratingsCollection.whereEqualTo("boatId", boatId)
        .whereEqualTo("userId", userId)

    val result = query.get().await()

    return if (!result.isEmpty) {
        // If a rating document is found, extract the rating value
        val ratingDocument = result.documents[0]
        val rating = ratingDocument.getDouble("rating")?.toFloat() ?: 0f
        rating
    } else {
        // If no rating document is found, return a default rating (e.g., 0)
        0f
    }
}

@Composable
fun RatingBar(
    rating: Float,
    maxRating: Int,
    onRatingChanged: ((Float) -> Unit)?
) {
    Row {
        for (i in 1..maxRating) {
            Icon(
                imageVector = if (i <= rating) Icons.Default.Star else Icons.Default.Star,
                contentDescription = null,
                tint = if (i <= rating) Color(0xFFE5A400) else Color.Gray,
                modifier = Modifier
                    .clickable {
                        if (onRatingChanged != null) {
                            onRatingChanged(i.toFloat())
                        }
                    }
                    .padding(4.dp)
            )
        }
    }
}