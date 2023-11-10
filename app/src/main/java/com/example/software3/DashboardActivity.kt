package com.example.software3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.software3.adapter.ProfileAdapter
import com.example.software3.model.Profile
import com.example.software3.seeder.ProfileSeeder
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DashboardActivity : AppCompatActivity() {
    private lateinit var profilesRecyclerView: RecyclerView
    private lateinit var profileAdapter: ProfileAdapter
    private val profileSeeder = ProfileSeeder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        profilesRecyclerView = findViewById(R.id.profilesRecyclerView)
        supportActionBar?.apply {
            title = "Dashboard"
            setDisplayHomeAsUpEnabled(false) // Hiermee wordt de up-knop weergegeven
        }

        // Stel een LayoutManager in voor de RecyclerView
        profilesRecyclerView.layoutManager = LinearLayoutManager(this)

        // Ophalen van profielgegevens van Firestore
        val db = FirebaseFirestore.getInstance()
        val usersCollection = db.collection("profiles")

        usersCollection.get()
            .addOnSuccessListener { querySnapshot ->
                val profiles = mutableListOf<Profile>()

                for (document in querySnapshot) {
                    val profile = Profile(
                        document.getString("username") ?: "",
                        document.getString("firstname") ?: "",
                        document.getString("lastname") ?: "",
                        document.getBoolean("visability") ?: false,
                        document.getString("role") ?: ""
                    )
                    profiles.add(profile)
                }

                profileAdapter = ProfileAdapter(profiles)
                profilesRecyclerView.adapter = profileAdapter
            }


//        seedDatabaseWithProfiles()

        val profileBtn = findViewById<Button>(R.id.profileBtn)
        profileBtn.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun seedDatabaseWithProfiles() {
        val numProfilesToSeed = 100 // Define the number of profiles you want to seed
        GlobalScope.launch(Dispatchers.IO) {
            try {
                profileSeeder.seedProfiles(numProfilesToSeed)
                // Handle success, if needed
            } catch (e: Exception) {
                // Handle any exceptions during seeding
                Log.e("Seeding", "Error seeding database: $e")
            }
        }
    }
}


