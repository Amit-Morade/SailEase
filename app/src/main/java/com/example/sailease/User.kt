package com.example.sailease

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.sailease.boatz



@Composable
fun User(navController: NavHostController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "List of Owned Boats",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(getRentedBoats()) { boat ->
                RentedBoatItem(boat = boat)
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
fun RentedBoatItem(boat: Boat) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
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

// Function to provide sample data of rented boats
fun getRentedBoats(): List<Boat> {
    return boatz // Return the boatz array
}
//@Composable
//fun User(navController: NavHostController) {
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        Text(
//            text = "List of Rented Boats",
//            style = MaterialTheme.typography.headlineLarge,
//            modifier = Modifier.padding(bottom = 16.dp)
//        )
//        // Assuming this is the list of rented boats
//
//        LazyColumn(
//            verticalArrangement = Arrangement.spacedBy(8.dp),
//            modifier = Modifier.weight(1f)
//        ) {
//            items(getRentedBoats()) { boat ->
//                RentedBoatItem(boat = boat)
//            }
//        }
//        // Add a button to navigate to another screen or perform an action
//        Button(
//            onClick = { /* TODO: Navigate or perform action */ },
//            modifier = Modifier.align(Alignment.End)
//        ) {
//            Text(text = "Next")
//        }
//    }
//}
//
//@Composable
//fun RentedBoatItem(boat: Boat2) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(150.dp),
//        shape = RoundedCornerShape(8.dp)
//    ) {
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier.padding(8.dp)
//        ) {
//            Column(
//                modifier = Modifier.weight(1f) // Text column takes most of the space
//            ) {
//                Text(
//                    text = boat.name,
//                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
//                )
//                Spacer(modifier = Modifier.height(4.dp))
//                Text(
//                    text = "Rented by: ${boat.renter}",
//                    style = MaterialTheme.typography.bodyLarge
//                )
//            }
//            Image(
//                painter = painterResource(id = R.drawable.sailing_yacht), // Replace with actual image resource
//                contentDescription = "Boat",
//                modifier = Modifier
//                    .size(100.dp)
//                    .clip(RoundedCornerShape(8.dp))
//            )
//        }
//    }
//}
//
//// Sample data class representing a rented boat
//data class Boat2(val name: String, val renter: String)
//
//// Function to provide sample data of rented boats
//fun getRentedBoats(): List<Boat2> {
//    return listOf(
//        Boat2(name = "XSailing Yatch", renter = "John Doe"),
//        Boat2(name = "XSpeed Boat", renter = "Jane Smith"),
//        Boat2(name = "XPantoon Boat", renter = "Alice Johnson"),
//        Boat2(name = "XSailing Yatch", renter = "John Doe"),
//        Boat2(name = "XSpeed Boat", renter = "Jane Smith"),
//        Boat2(name = "XPantoon Boat", renter = "Alice Johnson"),
//    )
//}
