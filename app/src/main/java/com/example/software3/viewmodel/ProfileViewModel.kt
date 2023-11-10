package com.example.software3.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.software3.model.Profile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileViewModel : ViewModel() {
    private val _profileData = MutableLiveData<Profile>()
    val profileData: LiveData<Profile>
        get() = _profileData

    init {
        val currentUser = FirebaseAuth.getInstance().currentUser

        // Controleer of de gebruiker is ingelogd
        if (currentUser != null) {
            val userId = currentUser.uid

            val db = FirebaseFirestore.getInstance()
            val userDocument = db.collection("profiles").document(userId)

            userDocument.addSnapshotListener { snapshot, _ ->
                if (snapshot != null && snapshot.exists()) {
                    val username = snapshot.getString("username") ?: ""
                    val firstName = snapshot.getString("firstname") ?: ""
                    val lastName = snapshot.getString("lastname") ?: ""
                    val visability = snapshot.getBoolean("visability") ?: false
                    val role = snapshot.getString("role") ?: ""

                    val profile = Profile(username, firstName, lastName, visability, role)
                    _profileData.value = profile
                }
            }
        }
    }

    fun updateVisibilityInDatabase(newVisibility: Boolean) {
        val user = FirebaseAuth.getInstance().currentUser

        user?.let { currentUser ->
            val userId = currentUser.uid
            val db = FirebaseFirestore.getInstance()
            val userDocument = db.collection("profiles").document(userId)

            userDocument.update("visability", newVisibility)
                .addOnSuccessListener {
                    val currentProfile = _profileData.value

                    // Check op null voordat je de waarde bijwerkt
                    val updatedProfile = currentProfile?.copy(visability = newVisibility)
                    _profileData.value = updatedProfile ?: Profile("", "", "", newVisibility, "") // Standaardwaarde instellen als currentProfile null is
                }
                .addOnFailureListener { exception ->
                    // Handle eventuele fouten bij het bijwerken van de database
                }
        } ?: run {
            // Handel de situatie af waarin de gebruiker niet is ingelogd
        }
    }

}


