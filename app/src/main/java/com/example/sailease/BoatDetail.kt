package com.example.sailease

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import java.util.UUID
import com.example.sailease.boatz
import com.example.sailease.BoatList
@Composable
fun BoatDetail(boatId: String?,  navController: NavHostController) {
    fun findBoatById(id: String?): Boat? {
        return sampleBoats.find { it.id == id }
    }
    val boat = findBoatById(boatId)
    var showFullDescription by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) } // State variable for dialog visibility

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column (
            modifier = Modifier
                .padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 80.dp)
                .fillMaxWidth().verticalScroll(enabled = true, state = rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,

        ) {
            boat?.let {
                Image(painter = painterResource(id = R.drawable.sailing_yacht), contentDescription = "Boat")
                Spacer(modifier = Modifier.height(10.dp)) // Add some spacing
                Text(
                    text = it.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Price: ${it.price}",
                    fontSize = 18.sp,
                    color = Color.Gray
                )
                Text(
                    text = "Availability: ${it.availability}",
                    fontSize = 18.sp,
                    color = Color.Gray
                )


                val singapore = LatLng(boat.latitude, boat.longitude)
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
                        text = it.description,
                        fontSize = 18.sp
                    )
                } else {
                    Text(
                        text = "${it.description.take(150)}...", // Show only first 100 characters
                        fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.height(10.dp)) // Add some spacing
                Text(
                    text = if (showFullDescription) "Show Less" else "Learn More",
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { showFullDescription = !showFullDescription }
                )

                Button(
                    onClick = {  navController.navigate("User")
                        boatz.add(Boat(
                            id = it.id, // Generate a unique ID
                            name = it.name,
                            price = it.price,
                            availability = it.availability,
                            description = it.description,
                            latitude = it.latitude, // Provide latitude value
                            longitude = it.longitude // Provide longitude value
                        ))
                        showDialog = true},
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Rent Now")
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
                text = "Boat not found",
                fontSize = 18.sp,
                color = Color.Red
            )
        }
    }
}

private fun findBoatById(id: String?): Boat? {
    return sampleBoats.find { it.id == id }
}
