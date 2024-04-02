package com.example.sailease

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun Settings(navController: NavController, viewModel: SignInViewModel) {
    val currentUser = remember { FirebaseAuth.getInstance().currentUser }
    val signOutDialogState = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    val emailState = remember { mutableStateOf(currentUser?.email ?: "") }
    val ratingState = remember { mutableStateOf("5") } // Default rating
    val ageState = remember { mutableStateOf("") }
    val sexState = remember { mutableStateOf("") }

    val editMode = remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val sexOptions = listOf("M", "F", "Other")
    var expanded by remember { mutableStateOf(false) }
    var selectedSex by remember { mutableStateOf("") }
    if (currentUser != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                painter = painterResource(id = R.drawable.pp), // Replace with user's profile picture
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 16.dp),
                        alignment = Alignment.Center
            )
                    OutlinedTextField(
                        value = emailState.value,
                        onValueChange = { },
                        label = { Text("Email") },
                        enabled = false,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = ratingState.value,
                        onValueChange = { },
                        label = { Text("Rating") },
                        enabled = false,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = ageState.value,
                        onValueChange = { ageState.value = it },
                        label = { Text("Age") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        enabled = editMode.value,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = sexState.value,
                        onValueChange = { sexState.value = it },
                        label = { Text("Sex") },
                        enabled = editMode.value,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))



                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = { editMode.value = true },
                    enabled = !editMode.value
                ) {
                    Text(text = "Change Properties")
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = { editMode.value = false },
                    enabled = editMode.value
                ) {
                    Text(text = "Save Changes")
                }


                Spacer(modifier = Modifier.weight(1f))


            }
            Button(onClick = { signOutDialogState.value = true }) {
                Text(text = "Sign Out")
            }
        }

        if (signOutDialogState.value) {
            AlertDialog(
                onDismissRequest = { signOutDialogState.value = false },
                title = { Text(text = "Sign Out") },
                text = { Text(text = "Are you sure you want to sign out?") },
                confirmButton = {
                    Button(onClick = {
                        coroutineScope.launch {
                            viewModel.onSignOut()
                            navController.navigate(Screen.Login.route)
                        }
                    }) {
                        Text(text = "Confirm")
                    }
                },
                dismissButton = {
                    Button(onClick = { signOutDialogState.value = false }) {
                        Text(text = "Cancel")
                    }
                }
            )
        }
    } else {
        // Navigate to login screen if user is not signed in
        LaunchedEffect(Unit) {
            navController.navigate(Screen.Login.route)
        }
    }
}
//@Composable
//fun Settings(navController: NavController, viewModel: SignInViewModel) {
//    val currentUser = remember { FirebaseAuth.getInstance().currentUser }
//    val signOutDialogState = remember { mutableStateOf(false) }
//    val coroutineScope = rememberCoroutineScope()
//
//    if (currentUser != null) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Image(
//                painter = painterResource(id = R.drawable.pp), // Replace with user's profile picture
//                contentDescription = "Profile Picture",
//                modifier = Modifier
//                    .size(120.dp)
//                    .padding(bottom = 16.dp)
//            )
//            Text(text = "Name: User")
//            Text(text = "Email: ${currentUser.email}")
//            Spacer(modifier = Modifier.height(16.dp))
//            Button(onClick = { signOutDialogState.value = true }) {
//                Text(text = "Sign Out")
//            }
//        }
//
//        if (signOutDialogState.value) {
//            AlertDialog(
//                onDismissRequest = { signOutDialogState.value = false },
//                title = { Text(text = "Sign Out") },
//                text = { Text(text = "Are you sure you want to sign out?") },
//                confirmButton = {
//                    Button(onClick = {
//                        coroutineScope.launch {
//                            viewModel.onSignOut()
//                            navController.navigate(Screen.Login.route)
//                        }
//                    }) {
//                        Text(text = "Confirm")
//                    }
//                },
//                dismissButton = {
//                    Button(onClick = { signOutDialogState.value = false }) {
//                        Text(text = "Cancel")
//                    }
//                }
//            )
//        }
//    } else {
//        // Navigate to login screen if user is not signed in
//        LaunchedEffect(Unit) {
//            navController.navigate(Screen.Login.route)
//        }
//    }

//
//    val currentuser = remember { mutableStateOf(FirebaseAuth.getInstance().currentUser) }
//    if(currentuser.value != null) {
//        Text(text = currentuser.value?.email + "")
//    }else {
//        Text(text = "null")
//        navController.navigate(Screen.Login.route)
//
//    }
//
//    Button(modifier = Modifier.padding(50.dp),onClick = {
//        viewModel.onSignOut()
//        navController.navigate(route = Screen.Login.route)
//    } ) {
//        Text(text = "SignOut")
//
//    }
//}

