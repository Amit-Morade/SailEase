package com.example.sailease

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

import java.util.*

data class Rating(
    val boatId: String,
    val rating: Float,
    val timestamp: Date,
    val userId: String
)


@Composable
fun RatingsList(boatId: String) {
    val ratings = remember { mutableStateListOf<Rating>() }

    // Fetch ratings for the boat
    LaunchedEffect(Unit) {
        val fetchedRatings = fetchAllRatingsForBoat(boatId)
        ratings.clear()
        ratings.addAll(fetchedRatings)
    }

    // Use Box with Column to achieve scrolling
    Box(modifier = Modifier.fillMaxSize()) {
        Column(

        ) {
            ratings.forEachIndexed { index, rating ->
                RatingItem(rating, index+1)
            }
        }
    }
}


@Composable
fun RatingItem(rating: Rating, index: Number) {
    // Display rating information here
    Text(text = "User: ${index}, Rating: ${rating.rating}")
    RatingBar(
        rating = rating.rating,
        maxRating = 5,
        onRatingChanged = null
    )
}


suspend fun fetchAllRatingsForBoat(boatId: String): List<Rating> {
    val ratingsCollection = Firebase.firestore.collection("boatRentalRatings")
    val query = ratingsCollection.whereEqualTo("boatId", boatId)

    val result = query.get().await()

    val ratingsList = mutableListOf<Rating>()

    for (document in result) {
        val rating = Rating(
            boatId = document.getString("boatId") ?: "",
            rating = document.getDouble("rating")?.toFloat() ?: 0f,
            timestamp = (document.get("timestamp") as? Timestamp)?.toDate() ?: Date(),
            userId = document.getString("userId") ?: ""
        )
        ratingsList.add(rating)
    }

    return ratingsList
}