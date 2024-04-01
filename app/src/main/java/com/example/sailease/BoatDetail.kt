package com.example.sailease

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BoatDetail(boatId: String?) {
    val boat = findBoatById(boatId)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column (
            modifier = Modifier
                .padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 80.dp)
                .fillMaxWidth().verticalScroll(enabled = true, state = rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,

        ) {
            boat?.let {
                Image(painter = painterResource(id = R.drawable.sailing_yacht), contentDescription = "Boat")
                Text(
                    text = it.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Price: ${it.price}",
                    fontSize = 18.sp,
                    color = Color.Gray
                )
                Text(
                    text = "Availability: ${it.availability}",
                    fontSize = 18.sp,
                    color = Color.Gray
                )
                Text(text = "Description: " + "\n" + "${it.description}",
                    fontSize = 18.sp)
            } ?: Text(
                text = "Boat not found",
                fontSize = 18.sp,
                color = Color.Red
            )
        }
    }
}

private fun findBoatById(id: String?): Boat? {
    return sampleBoats.find { it.id == id }
}
