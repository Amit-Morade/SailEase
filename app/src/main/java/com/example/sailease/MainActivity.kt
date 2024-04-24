package com.example.sailease

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.sailease.ui.theme.SailEaseTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.FirebaseApp
import com.example.sailease.lat
import com.example.sailease.long
data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val onClick: String
)

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    lateinit var navController: NavHostController
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var locationRequired: Boolean = false
//    private var latitude: Double = 0.0
//    private var longitude: Double = 0.0
//Initialize it where you need it
    private val permissions = arrayOf(
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )
    override fun onResume() {
        super.onResume()
        if(locationRequired){
            startLocationUpdates()
        }
    }

    override fun onPause() {
        super.onPause()
        locationCallback?.let {
            fusedLocationProviderClient?.removeLocationUpdates(it)
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        locationCallback?.let {
            val locationRequest = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,100
            )
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(3000)
                .setMaxUpdateDelayMillis(100)
                .build()

            fusedLocationProviderClient?.requestLocationUpdates(
                locationRequest,
                it,
                Looper.getMainLooper()
            )
        }
    }
//

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
//        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this)
        FirebaseApp.initializeApp(this)
        setContent {
            var currentLocation by remember {
                mutableStateOf(LatLng(0.toDouble(),0.toDouble()))
            }
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult) {
                    super.onLocationResult(p0)
                    for (location in p0.locations){
                        currentLocation = LatLng(location.latitude,location.longitude)
                        lat = location.latitude
                        long = location.longitude
                    }
                }
            }
            if(lat==0.0){
                fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this)
                LocationScreen(this@MainActivity,currentLocation)
            }
            else {


//            LocationScreen(this@MainActivity,currentLocation)


                SailEaseTheme {
                    navController = rememberNavController();
                    val items = listOf(
                        BottomNavigationItem(
                            title = "Home",
                            selectedIcon = Icons.Filled.Home,
                            unselectedIcon = Icons.Outlined.Home,
                            onClick = Screen.Home.route
                        ),

                        BottomNavigationItem(
                            title = "Add",
                            selectedIcon = Icons.Filled.Add,
                            unselectedIcon = Icons.Outlined.Add,
                            onClick = Screen.Rent.route
                        ),
                        BottomNavigationItem(
                            title = "User",
                            selectedIcon = Icons.Filled.AccountBox,
                            unselectedIcon = Icons.Outlined.AccountBox,
                            onClick = Screen.User.route
                        ),
                        BottomNavigationItem(
                            title = "Settings",
                            selectedIcon = Icons.Filled.Settings,
                            unselectedIcon = Icons.Outlined.Settings,
                            onClick = Screen.Settings.route
                        )
                    )

                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {


                        Scaffold(
//                        topBar = {
//                            TopAppBar(
//                                title = { Text("Boat Finder") },
//                                navigationIcon = {
//                                    Image(
//                                        painter = painterResource(id = R.drawable.boat),
//                                        contentDescription = null,
//                                        modifier = Modifier
//                                            .padding(horizontal = 12.dp)
//                                            .size(58.dp)
//
//                                    )
//                                }
//                            )
//                        },
                            bottomBar = {
                                MyNavBar(items = items, navController)
                            }
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize()
//                                .padding(top = 60.dp)
                            ) {
                                SetNavGraph(navController = navController)
                            }

                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun LocationScreen(context: MainActivity, currentLocation: LatLng) {
        var isLoading by remember { mutableStateOf(true) }
        val launchMultiplePermissions = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()) {
                permissionMaps->
            val areGranted = permissionMaps.values.reduce{acc, next-> acc && next}
            if (areGranted){
                locationRequired = true
                startLocationUpdates()
                Toast.makeText(context,"Permission Granted", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(context,"Permission Denied", Toast.LENGTH_SHORT).show()
            }
            isLoading = false
        }

        LaunchedEffect(Unit) {
            // Code inside this block runs when the Composable appears on the screen
            if (permissions.all{
                    ContextCompat.checkSelfPermission(context,it) == PackageManager.PERMISSION_GRANTED
                }){
                startLocationUpdates()

            }
            else{
                launchMultiplePermissions.launch(permissions)
            }

        }

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Loading...",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }

    }
}



