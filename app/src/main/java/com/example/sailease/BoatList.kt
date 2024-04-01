package com.example.sailease

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

// Define your data model for Boat
data class Boat(
    val id: String,
    val name: String,
//    val imageResId: Int,
    val price: String,
    val availability: String,
    val location: Location

)

data class Location(
    val latitude: Double,
    val longitude: Double
)

@Composable
fun BoatList(boats: List<Boat>, navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(bottom = 80.dp)
    ) {
        items(boats) { boat ->
            BoatItem(boat, onItemClick = {
                navController.navigate("boatDetail/${boat.id}")
            })
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun BoatItem(boat: Boat, onItemClick: () -> Unit) {
    Card(
        modifier = Modifier
            .background(color = Color.Blue)
            .fillMaxWidth()
            .height(150.dp)
            .clickable(onClick = onItemClick),
        shape = RoundedCornerShape(8.dp),

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
//            Image(
//                painter = painterResource(id = boat.imageResId),
//                contentDescription = "Boat Image",
//                modifier = Modifier
//                    .size(100.dp)
//                    .padding(end = 16.dp),
//                contentScale = ContentScale.Crop
//            )

            Column {
                Text(text = boat.name)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Price: ${boat.price}")
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Availability: ${boat.availability}",
                    color = if (boat.availability == "Available") Color.Green else Color.Red
                )
            }
        }
    }
}

