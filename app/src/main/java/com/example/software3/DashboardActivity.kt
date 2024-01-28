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
            title = "Dashboard / Landingspagina"
            setDisplayHomeAsUpEnabled(false) // Hiermee wordt de up-knop weergegeven
        }

        profilesRecyclerView.layoutManager = LinearLayoutManager(this)

        val db = FirebaseFirestore.getInstance()
        val usersCollection = db.collection("profiles")

        usersCollection.get()
            .addOnSuccessListener { querySnapshot ->
                val profiles = mutableListOf<Profile>()
                for (document in querySnapshot) {
                    val visability = document.getBoolean("visability") ?: false
                    if (visability) { // Voeg alleen profielen toe als visability waar is
                        val profile = Profile(
                            username = document.getString("username") ?: "",
                            firstname = document.getString("firstname") ?: "",
                            lastname = document.getString("lastname") ?: "",
                            visability = visability,
                            role = document.getString("role") ?: ""
                        )
                        profiles.add(profile)
                    }
                }

                profileAdapter = ProfileAdapter(profiles)
                profilesRecyclerView.adapter = profileAdapter
            }

        val profileBtn = findViewById<Button>(R.id.profileBtn)
        profileBtn.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

//        seedDatabaseWithProfiles()
    }

    private fun seedDatabaseWithProfiles() {
        val numProfilesToSeed = 100
        GlobalScope.launch(Dispatchers.IO) {
            try {
                profileSeeder.seedProfiles(numProfilesToSeed)
            } catch (e: Exception) {
                Log.e("Seeding", "Error seeding database: $e")
            }
        }
    }
}
