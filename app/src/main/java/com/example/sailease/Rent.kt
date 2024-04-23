package com.example.sailease
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable


import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Dialog
import java.util.UUID
import com.example.sailease.long
import com.example.sailease.lat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


//class Rent {
//    fun rentNearLocation(location: LatLng) {
//        // Do something with the location
//        println("Renting near location: $location")
//    }
//}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Rent(navController: NavHostController) {
    // State for storing form data
    val boatNameState = remember { mutableStateOf("") }
    val priceState = remember { mutableStateOf("") }
    val availabilityState = remember { mutableStateOf("") }
    val descriptionState = remember { mutableStateOf("") }
    val lattitude = remember { mutableStateOf("") }
    val longitute = remember { mutableStateOf("") }
    // State for showing/hiding the dialog
    val showDialog = remember { mutableStateOf(false) }
    val showErrorDialog = remember { mutableStateOf(false) }
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid // Return empty list if user is not authenticated
    val coroutineScope = rememberCoroutineScope()
    fun isFormValid(): Boolean {
        return boatNameState.value.isNotEmpty() &&
                priceState.value.isNotEmpty() &&
                availabilityState.value.isNotEmpty() &&
                descriptionState.value.isNotEmpty()
    }
        // Apply vertical scroll modifier to the Column
        LazyColumn(
            modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally,
            content = {
                item {
                    Text(
                        text = "Rent a Boat",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Image(
                        painter = painterResource(id = R.drawable.sailing_yacht),
                        contentDescription = "Boat"
                    )
                    Spacer(modifier = Modifier.padding(5.dp))
                    OutlinedTextField(
                        value = boatNameState.value,
                        onValueChange = { boatNameState.value = it },
                        label = { Text("Boat Name") },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.padding(5.dp))
                    OutlinedTextField(
                        value = priceState.value,
                        onValueChange = { priceState.value = it },
                        label = { Text("Price") },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.padding(5.dp))
                    OutlinedTextField(
                        value = availabilityState.value,
                        onValueChange = { availabilityState.value = it },
                        label = { Text("Availability") },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.padding(5.dp))

                    OutlinedTextField(
                        value = lat.toString(),
                        onValueChange = { lattitude.value = it },
                        label = { Text("Lattitude") },
                        modifier = Modifier
                            .fillMaxWidth()
                        // Set a fixed height,

                    )
                    Spacer(modifier = Modifier.padding(5.dp))
                    OutlinedTextField(
                        value = long.toString(),
                        onValueChange = { longitute.value = it },
                        label = { Text("Longitute") },
                        modifier = Modifier
                            .fillMaxWidth()
                        // Set a fixed height,

                    )
                    Spacer(modifier = Modifier.padding(5.dp))
                    OutlinedTextField(
                        value = descriptionState.value,
                        onValueChange = { descriptionState.value = it },
                        label = { Text("Description") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp), // Set a fixed height,
                        maxLines = 8
                    )

                    Spacer(modifier = Modifier.padding(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center // Center the Button horizontally
                    ) {
                        Button(
                            onClick = {
                                // Add the input data to the sampleBoats list
                                if (isFormValid()) {
//                                    boatz.add(Boat(
//                                        id = UUID.randomUUID().toString(), // Generate a unique ID
//                                        name = boatNameState.value,
//                                        price = "$"+priceState.value,
//                                        availability = availabilityState.value,
//                                        description = descriptionState.value,
//                                        latitude = 45.4215, // Provide latitude value
//                                        longitude = -75.6981 // Provide longitude value
//                                    ))

                                    val boat = Boat(
                                        name = boatNameState.value,
                                        price = priceState.value,
                                        availability = availabilityState.value,
                                        description = descriptionState.value,
                                        latitude = lat?: 0.0, // Replace 0.0 with default latitude value
                                        longitude = long?: 0.0, // Replace 0.0 with default longitude value
                                        rented = false,
                                        ownerId = userId.toString()
                                    )

                                    val db = FirebaseFirestore.getInstance()
                                    val boatsCollection = db.collection("boats")

                                    // Add the boat to the "boats" collection
                                    try {
                                        coroutineScope.launch {
                                            addBoatToFirestore(boat)

                                            // Boat added successfully, show success message or navigate to another screen
                                        }
                                    } catch (e: Exception) {
                                        // Handle error: show error message or log the error
                                    }

                                // Clear input fields after submission
                                boatNameState.value = ""
                                priceState.value = ""
                                availabilityState.value = ""
                                descriptionState.value = ""
                                showDialog.value = true
                            }else {
                                    // Show error dialog if form is not valid
                                    showErrorDialog.value = true
                                }},
                            modifier = Modifier
                                .padding(bottom = 80.dp)

                        ) {
                            Text("Post")
                        }
                    }
                    if (showDialog.value) {
                        Dialog(
                            onDismissRequest = { showDialog.value = false },
                            content = {
                                Surface( // Wrap content with Surface
                                    color = Color.White // Set background color to white
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .fillMaxWidth(), // Fill the width of the dialog
                                        horizontalAlignment = Alignment.CenterHorizontally // Align items horizontally center
                                    ) {
                                        Text("Post Submitted Successfully!", modifier = Modifier.padding(bottom = 16.dp))
                                        Button(
                                            onClick = { showDialog.value = false }
                                        ) {
                                            Text("OK", modifier = Modifier.clickable { navController.navigate(Screen.ManageBoats.route) })
                                        }
                                    }
                                }
                            }
                        )
                    }
                    if (showErrorDialog.value) {
                        Dialog(
                            onDismissRequest = { showErrorDialog.value = false },
                            content = {
                                Surface( // Wrap content with Surface
                                    color = Color.White // Set background color to white
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .fillMaxWidth(), // Fill the width of the dialog
                                        horizontalAlignment = Alignment.CenterHorizontally // Align items horizontally center
                                    ) {
                                        Text("Please fill all the fields!", modifier = Modifier.padding(bottom = 16.dp))
                                        Button(
                                            onClick = { showErrorDialog.value = false
                                                 }
                                        ) {
                                            Text("OK")
                                        }
                                    }
                                }
                            }
                        )
                    }

                }
            }
        )


}

suspend fun addBoatToFirestore(boat: Boat) {
    val db = FirebaseFirestore.getInstance()
    val boatsCollection = db.collection("boats")

    // Add the boat to the "boats" collection
    val documentRef = boatsCollection.add(boat).await()
    val boatId = documentRef.id // Get the document ID

    // Update the boat document with the boatId field
    boatsCollection.document(documentRef.id)
        .set(mapOf("boatId" to boatId), SetOptions.merge()) // Merge to update only the boatId field

}