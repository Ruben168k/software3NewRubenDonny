package com.example.software3.seeder

import android.util.Log
import com.example.software3.model.Profile
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.github.javafaker.Faker

class ProfileSeeder {
    private val faker = Faker()

    suspend fun seedProfiles(numProfiles: Int) {
        val db = FirebaseFirestore.getInstance()
        val profilesCollection = db.collection("profiles")

        for (i in 1..numProfiles) {
            val firstName = faker.name().firstName()
            val lastName = faker.name().lastName()
            val profile = Profile(
                username = faker.name().username(),
                firstname = firstName,
                lastname = lastName,
                visability = faker.bool().bool(),
                role = if (faker.bool().bool()) "user" else "admin"  // Eenvoudige keuze tussen twee rollen
            )

            try {
                profilesCollection.add(profile).await()
                Log.d("Seeding", "Profile $i toegevoegd: $firstName $lastName")
            } catch (e: Exception) {
                Log.e("Seeding", "Fout bij toevoegen van profile $i: $e")
            }
        }
    }
}


