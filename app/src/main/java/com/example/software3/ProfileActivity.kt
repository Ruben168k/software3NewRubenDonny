package com.example.software3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.software3.viewmodel.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {
    private lateinit var viewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val auth = FirebaseAuth.getInstance()

        if (auth.currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        supportActionBar?.apply {
            title = "Mijn Profiel"
            setDisplayHomeAsUpEnabled(true)
        }

        val usernameEditText: EditText = findViewById(R.id.usernameEditText)
        val firstNameEditText: EditText = findViewById(R.id.firstNameEditText)
        val lastNameEditText: EditText = findViewById(R.id.lastNameEditText)
        val visibilitySwitch: Switch = findViewById(R.id.visibilitySwitch)
        val saveChangesButton: Button = findViewById(R.id.saveChangesButton)
        val logoutButton: Button = findViewById(R.id.logoutButton)

        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        viewModel.profileData.observe(this) { profile ->
            usernameEditText.setText(profile.username)
            firstNameEditText.setText(profile.firstname)
            lastNameEditText.setText(profile.lastname)
            visibilitySwitch.isChecked = profile.visability
        }

        visibilitySwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateVisibilityInDatabase(isChecked)
        }

        saveChangesButton.setOnClickListener {
            val updatedUsername = usernameEditText.text.toString()
            val updatedFirstName = firstNameEditText.text.toString()
            val updatedLastName = lastNameEditText.text.toString()

            // Aanname: een methode om de gebruikersnaam, voornaam en achternaam bij te werken.
            // Deze methode moet in je ViewModel geïmplementeerd zijn.
            viewModel.updateProfileData(updatedUsername, updatedFirstName, updatedLastName)
        }

        logoutButton.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }

        val deleteAccountButton: Button = findViewById(R.id.deleteAccountButton)
        deleteAccountButton.setOnClickListener {
            deleteAccount()
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
        return true
    }

    private fun deleteAccount() {
        val user = FirebaseAuth.getInstance().currentUser

        user?.delete()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Fout bij het verwijderen van account.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
