package com.example.sailease

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun BoatDetail(boatId: String?) {
    fun findBoatById(id: String?): Boat? {
        return sampleBoats.find { it.id == id }
    }
    val boat = findBoatById(boatId)
    var showFullDescription by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column (
            modifier = Modifier
                .padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 80.dp)
                .fillMaxWidth().verticalScroll(enabled = true, state = rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,

        ) {
            boat?.let {
                Image(painter = painterResource(id = R.drawable.sailing_yacht), contentDescription = "Boat")
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


                val singapore = LatLng(1.35, 103.87)
                val cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(singapore, 10f)
                }

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

                if (showFullDescription) {
                    Text(
                        text = it.description,
                        fontSize = 18.sp
                    )
                } else {
                    Text(
                        text = "${it.description.take(250)}...", // Show only first 100 characters
                        fontSize = 18.sp
                    )
                }

                Button(
                    onClick = { showFullDescription = !showFullDescription },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(if (showFullDescription) "Show Less" else "Learn More")
                }

                Button(
                    onClick = { /* Handle button click */ },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Rent Now")
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
