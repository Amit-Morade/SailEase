package com.example.sailease.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sailease.Boat
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore

class BoatViewModel : ViewModel() {
    private val boatListLiveData = MutableLiveData<List<Boat>>()

    init {
        fetchBoatList()
    }

    fun getBoatList(): LiveData<List<Boat>> {
        return boatListLiveData
    }

    private fun fetchBoatList() {
        val firestore = FirebaseFirestore.getInstance()
        val boatsCollection = firestore.collection("boats")

        boatsCollection.get().addOnSuccessListener { querySnapshot ->
            val boats = mutableListOf<Boat>()
            for (document in querySnapshot.documents) {
                val boat = document.toObject(Boat::class.java)
                boat?.let { boats.add(it) }
            }
            boatListLiveData.value = boats
        }.addOnFailureListener { exception ->
            // Handle failure
        }
    }
}
