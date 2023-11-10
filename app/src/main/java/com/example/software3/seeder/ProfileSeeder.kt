package com.example.software3.seeder

import android.util.Log
import com.example.software3.model.Profile
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProfileSeeder {
    suspend fun seedProfiles(numProfiles: Int) {
        val db = FirebaseFirestore.getInstance()
        val profilesCollection = db.collection("profiles")

        for (i in 1..numProfiles) {
            val profile = Profile(
                username = "user$i",
                firstname = "First$i",
                lastname = "Last$i",
                visability = true,
                role = "user"
            )

            try {
                profilesCollection.add(profile).await()
                Log.d("Seeding", "Profile $i toegevoegd")
            } catch (e: Exception) {
                Log.e("Seeding", "Fout bij toevoegen van profile $i: $e")
            }
        }
    }
}
