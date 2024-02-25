package com.example.sailease

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun Welcome(navController: NavController) {
    Box(
        modifier = with (Modifier){
            fillMaxSize()
                .paint(
                    // Replace with your image id
                    painterResource(id = R.drawable.main),
                    contentScale = ContentScale.FillBounds)

        })
    Column (modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(painter = painterResource(id = R.drawable.boat), contentDescription = "Boat Icon")
        Text(
            fontSize = 30.sp,
            text = "Sail Ease",
            modifier = Modifier.padding(bottom = 16.dp),
            fontWeight = FontWeight.Bold
        )

        Row {
            Button(onClick = { navController.navigate(route = Screen.Login.route)},
                modifier = Modifier.padding(10.dp)) {
                Text(text = "Login")
            }
            Button(onClick = { navController.navigate(route = Screen.Sign.route)},
                modifier = Modifier.padding(10.dp)) {
                Text(text = "Sign Up")
            }
        }
    }

}