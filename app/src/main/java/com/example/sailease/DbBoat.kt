package com.example.sailease

data class DbBoat(
    val id: String = "",
    val name: String ="",
//    val imageResId: Int,
    val price: String = "",
    var ownerId: String = "",
    val availability: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val description: String = "",
    var rented: Boolean = false
)