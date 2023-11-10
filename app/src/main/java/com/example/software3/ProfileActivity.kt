package com.example.software3
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.software3.viewmodel.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {
    private lateinit var viewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Binnen een activiteit, bijvoorbeeld in de onCreate-methode
        val auth = FirebaseAuth.getInstance()

        if (auth.currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish() // Optioneel: Sluit huidige activiteit om terugkeren te voorkomen
        }

        supportActionBar?.apply {
            title = "Mijn Profiel"
            setDisplayHomeAsUpEnabled(true) // Hiermee wordt de up-knop weergegeven
        }

        val usernameTextView: TextView = findViewById(R.id.usernameTextView)
        val firstNameTextView: TextView = findViewById(R.id.firstNameTextView)
        val lastNameTextView: TextView = findViewById(R.id.lastNameTextView)
        val visabilitySwitch: Switch = findViewById(R.id.visabilitySwitch)

        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        viewModel.profileData.observe(this) { profile ->
            usernameTextView.text = profile.username
            firstNameTextView.text = profile.firstname
            lastNameTextView.text = profile.lastname
            visabilitySwitch.isChecked = profile.visability
            visabilitySwitch.setOnCheckedChangeListener { _, isChecked ->
                viewModel.updateVisibilityInDatabase(isChecked)
            }

        }

        val logoutBtn = findViewById<Button>(R.id.logoutButton)
        val firebaseAuth = FirebaseAuth.getInstance()

        logoutBtn.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this, DashboardActivity::class.java))
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val upIntent = Intent(this, DashboardActivity::class.java)
        startActivity(upIntent)
        finish()
        return true
    }

}
