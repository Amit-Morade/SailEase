package com.example.sailease

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sailease.model.BoatViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore


// Define your data model for Boat
data class Boat(
    var boatId: String = "",
    var ownerId: String = "",
    val name: String ="",
//    val imageResId: Int,
    val price: String = "",
    val availability: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val description: String = "",
    var rented: Boolean = false
)


data class Location(
    val latitude: Double,
    val longitude: Double
)

@Composable
fun BoatList(boats: List<Boat>, navController: NavController) {
//    val boatList by boatViewModel.getBoatList().observeAsState(emptyList())
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid // Return empty list if user is not authenticated


    var searchText by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(bottom = 80.dp)) {

        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .clickable { navController.navigate(Screen.ManageBoats.route) }
        ) {
            Box(
                modifier = Modifier
                    .background(Color.LightGray) // Set background color of the box
                    .padding(12.dp) // Add padding to the box
            ) {
                Text(
                    text = "Manage Your Boats",
                    fontSize = 18.sp, // Set the font size
                    fontWeight = FontWeight.Bold, // Make the text bold
                )
            }
            Spacer(modifier = Modifier.width(130.dp))


        }




        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            label = { Text("Search by Category") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") }
        )

        val availableBoats = boats.filter { it.ownerId!=userId && it.rented==false }

        if(availableBoats.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .padding(bottom = 80.dp)
            ) {
                items(availableBoats) { boat ->
                    BoatItem(boat, onItemClick = {
                        navController.navigate("boatDetail/${boat.boatId}")
                    })
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No boats available",
                    fontSize = 30.sp
                )
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
//    val availableBoats = boats.filter { !it.rented }
//    LazyColumn(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//            .padding(bottom = 80.dp)
//    ) {
//        items(availableBoats) { boat ->
//            BoatItem(boat, onItemClick = {
//                navController.navigate("boatDetail/${boat.boatId}")
//            })
//            Spacer(modifier = Modifier.height(16.dp))
//        }
//    }
//}


@Composable
fun BoatItem(boat: Boat, onItemClick: () -> Unit, showRented: Boolean = false) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onItemClick)
            .height(150.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f) // Text column takes most of the space
            ) {
                if(showRented && boat.rented) {
                    Text(text = "Rented")
                }
                Text(
                    text = boat.name,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Price: ${boat.price}",
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Availability: ${boat.availability}",

                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = if (boat.availability == "Available") Color.Gray else Color.Gray
                    )
                )
            }
            Image(
                painter = painterResource(id = R.drawable.sailing_yacht),
                contentDescription = "boat",
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(20.dp))
            )
        }
    }
}
