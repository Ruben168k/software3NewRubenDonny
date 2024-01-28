package com.example.software3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.software3.model.Profile

class SignUpViewModel : ViewModel() {
    var username: String = ""
    var firstname: String = ""
    var lastname: String = ""
    var email: String = ""
    var password: String = ""

    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean>
        get() = _loginSuccess

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    fun onSignUpClick() {
        // Controleer of alle velden zijn ingevuld
        if (username.isBlank() || firstname.isBlank() || lastname.isBlank() || email.isBlank() || password.isBlank()) {
            _errorMessage.value = "Alle velden zijn verplicht."
            return
        }

        // Controleer de lengte van het wachtwoord
        if (password.length < 8) {
            _errorMessage.value = "Wachtwoord moet minstens 8 karakters bevatten."
            return
        }

        // Ga verder met de registratie
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.trim(), password.trim())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    user?.uid?.let { userId ->
                        val db = FirebaseFirestore.getInstance()
                        val userDocument = db.collection("profiles").document(userId)
                        val userData = Profile(username, firstname, lastname, true, "user")
                        userDocument.set(userData)
                            .addOnSuccessListener {
                                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener { signInTask ->
                                        if (signInTask.isSuccessful) {
                                            _loginSuccess.value = true
                                        } else {
                                            _errorMessage.value = "Inloggen na registratie mislukt: ${signInTask.exception?.localizedMessage}"
                                        }
                                    }
                            }
                            .addOnFailureListener {
                                _errorMessage.value = "Fout bij het aanmaken van gebruikersprofiel."
                            }
                    }
                } else {
                    _errorMessage.value = "Registratie mislukt: ${task.exception?.localizedMessage}"
                }
            }
    }

    fun onErrorMessageShown() {
        _errorMessage.value = null
    }
}
