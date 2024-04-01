package com.example.sailease

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

// Define your data model for Boat
data class Boat(
    val id: String,
    val name: String,
//    val imageResId: Int,
    val price: String,
    val availability: String,
    val location: Location,
    val description: String

)

data class Location(
    val latitude: Double,
    val longitude: Double
)

@Composable
fun BoatItem(boat: Boat, onItemClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onItemClick)
            .padding(8.dp)
            .height(200.dp), // Increased height for better visual
        shape = RoundedCornerShape(16.dp)
        // Adjusted corner radius for rounded edges // Added elevation for a lifted effect
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f) // Text column takes most of the space
                    .padding(end = 16.dp) // Added padding to separate text from image
            ) {
                Text(
                    text = boat.name,
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Price: ${boat.price}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Availability: ${boat.availability}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = if (boat.availability == "Available") Color.Magenta else Color.Red
                    )
                )
            }
            Image(
                painter = painterResource(id = R.drawable.sailing_yacht),
                contentDescription = "boat",
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(16.dp)) // Rounded corners for the image
            )
        }
    }
}


@Composable
fun BoatList(boats: List<Boat>, navController: NavController) {
    var searchText by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {

        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            label = { Text("Search") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            items(boats.filter {
                it.name.contains(searchText, ignoreCase = true) ||
                        it.description.contains(searchText, ignoreCase = true)
            }) { boat ->
                BoatItem(boat, onItemClick = {
                    navController.navigate("boatDetail/${boat.id}")
                })
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

//@Composable
//fun BoatList(boats: List<Boat>, navController: NavController) {
//    Image(
//        painter = painterResource(id = R.drawable.bg2),
//        contentDescription = "Background",
//        modifier = Modifier.fillMaxSize(),
//        contentScale = ContentScale.FillBounds // Adjust content scale as needed
//    )
//    LazyColumn(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//            .padding(bottom = 80.dp)
//    ) {
//        items(boats) { boat ->
//            BoatItem(boat, onItemClick = {
//                navController.navigate("boatDetail/${boat.id}")
//            })
//            Spacer(modifier = Modifier.height(16.dp))
//        }
//    }
//}
//@Composable
//fun BoatItem(boat: Boat, onItemClick: () -> Unit) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable(onClick = onItemClick)
//            .padding(8.dp)
//            .height(150.dp),
//        shape = RoundedCornerShape(8.dp)
//    ) {
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier.padding(8.dp)
//        ) {
//            Column(
//                modifier = Modifier
//                    .weight(1f) // Text column takes most of the space
//            ) {
//                Text(
//                    text = boat.name,
//                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
//                )
//                Spacer(modifier = Modifier.height(4.dp))
//                Text(
//                    text = "Price: ${boat.price}",
//                    style = MaterialTheme.typography.labelLarge
//                )
//                Spacer(modifier = Modifier.height(4.dp))
//                Text(
//                    text = "Availability: ${boat.availability}",
//
//                    style = MaterialTheme.typography.bodyMedium.copy(
//                        color = if (boat.availability == "Available") Color.Green else Color.Red
//                    )
//                )
//            }
//            Image(
//                painter = painterResource(id = R.drawable.sailing_yacht),
//                contentDescription = "boat",
//                modifier = Modifier
//                    .size(150.dp)
//                    .clip(RoundedCornerShape(20.dp))
//            )
//        }
//    }
//}

//@Composable
//fun BoatItem(boat: Boat, onItemClick: () -> Unit) {
//    Card(
//        modifier = Modifier
//            .background(color = Color.Blue)
//            .fillMaxWidth()
//            .height(150.dp)
//            .clickable(onClick = onItemClick),
//        shape = RoundedCornerShape(8.dp),
//
//    ) {
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier.padding(8.dp)
//        ) {
////            Image(
////                painter = painterResource(id = boat.imageResId),
////                contentDescription = "Boat Image",
////                modifier = Modifier
////                    .size(100.dp)
////                    .padding(end = 16.dp),
////                contentScale = ContentScale.Crop
////            )
//
//            Column {
//                Text(text = boat.name)
//                Spacer(modifier = Modifier.height(4.dp))
//                Text(text = "Price: ${boat.price}")
//                Spacer(modifier = Modifier.height(4.dp))
//                Text(
//                    text = "Availability: ${boat.availability}",
//                    color = if (boat.availability == "Available") Color.Green else Color.Red
//                )
//            }
//        }
//    }
//}
//
