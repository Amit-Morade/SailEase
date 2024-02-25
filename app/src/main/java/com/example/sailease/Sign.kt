package com.example.sailease

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun Sign(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Box(
        modifier = with (Modifier){
            fillMaxSize()
                .paint(
                    // Replace with your image id
                    painterResource(id = R.drawable.back),
                    contentScale = ContentScale.FillBounds)

        })
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            fontSize = 30.sp,
            text = "Sign Up",
            modifier = Modifier.padding(bottom = 16.dp),
            fontWeight = FontWeight.Bold
        )
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("First Name") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Last Name") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )
        Button(
            onClick = { navController.navigate(route = Screen.Home.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit")
        }
    }
//    Column(verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally,
//        modifier = Modifier.fillMaxSize().padding(50.dp)
//    ) {
//        Text(text = "Log In", fontSize = 35.sp, fontWeight = FontWeight.Bold)
//        var text =""
//        OutlinedTextField(
//            value = text,
//            onValueChange = { text = it },
//            label = { Text("Email") }
//        )
//        OutlinedTextField(
//            value = text,
//            onValueChange = { text = it },
//            label = { Text("Password") }
//        )
//        Button(onClick = { navController.navigate(route = Screen.Home.route)}) {
//            Text(text = "Submit")
//        }


//    }




}
