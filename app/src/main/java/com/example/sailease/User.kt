package com.example.sailease

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun User(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "List of Rented Boats",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        // Assuming this is the list of rented boats
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
fun RentedBoatItem(boat: Boat2) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = boat.name, style = MaterialTheme.typography.headlineMedium)
            Text(text = "Rented by: ${boat.renter}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

// Sample data class representing a rented boat
data class Boat2(val name: String, val renter: String)

// Function to provide sample data of rented boats
fun getRentedBoats(): List<Boat2> {
    return listOf(
        Boat2(name = "Boat 1", renter = "John Doe"),
        Boat2(name = "Boat 2", renter = "Jane Smith"),
        Boat2(name = "Boat 3", renter = "Alice Johnson")
    )
}
