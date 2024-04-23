package com.example.sailease

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng

class LocationUtils{
    lateinit var locationCallback: LocationCallback

}
@SuppressLint("MissingPermission")
private fun startLocationUpdates() {
    lateinit var locationCallback: LocationCallback
    locationCallback?.let {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,100
        )
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(3000)
            .setMaxUpdateDelayMillis(100)
            .build()

        lateinit var fusedLocationProviderClient: FusedLocationProviderClient
        fusedLocationProviderClient?.requestLocationUpdates(
            locationRequest,
            it,
            Looper.getMainLooper()
        )
    }
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun LocationScreen(context: MainActivity, currentLocation: LatLng) {
    var locationRequired: Boolean = false
    val permissions = arrayOf(
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )
    val launchMultiplePermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()) {
            permissionMaps->
        val areGranted = permissionMaps.values.reduce{acc, next-> acc && next}
        if (areGranted){
            startLocationUpdates()
            Toast.makeText(context,"Permission Granted", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(context,"Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    Box(modifier = Modifier.fillMaxSize()){
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = "Location ${currentLocation.latitude} and ${currentLocation.longitude}")
            Button(onClick = {
                if (permissions.all{
                        ContextCompat.checkSelfPermission(context,it) == PackageManager.PERMISSION_GRANTED
                    }){
                    startLocationUpdates()
                }
                else{
                    launchMultiplePermissions.launch(permissions)
                }
            }) {
                Text(text = "Get Location ")
            }
        }
    }
}