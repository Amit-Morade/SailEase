package com.example.sailease

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun BoatDetail(boatId: String?) {
    fun findBoatById(id: String?): Boat? {
        return sampleBoats.find { it.id == id }
    }

    var boat = findBoatById(boatId)
    Text(text = boat?.name + "")
}