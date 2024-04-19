package com.example.sailease
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


import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Dialog
import java.util.UUID
import com.example.sailease.sampleBoats
import com.example.sailease.boatz

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Rent(navController: NavHostController) {
    // State for storing form data
    val boatNameState = remember { mutableStateOf("") }
    val priceState = remember { mutableStateOf("") }
    val availabilityState = remember { mutableStateOf("") }
    val descriptionState = remember { mutableStateOf("") }
    // State for showing/hiding the dialog
    val showDialog = remember { mutableStateOf(false) }
    val showErrorDialog = remember { mutableStateOf(false) }


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
                                sampleBoats.add(
                                    Boat(
                                        id = UUID.randomUUID().toString(), // Generate a unique ID
                                        name = boatNameState.value,
                                        price = "$"+priceState.value,
                                        availability = availabilityState.value,
                                        description = descriptionState.value,
                                        latitude = 45.4215, // Provide latitude value
                                        longitude = -75.6981 // Provide longitude value
                                    )
                                )
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
                            Text("Submit")
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
                                            Text("OK")
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
