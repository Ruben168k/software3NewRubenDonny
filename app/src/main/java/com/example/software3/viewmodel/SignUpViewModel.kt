package com.example.software3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.util.Log
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

    fun onSignUpClick() {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.toString().trim(), password.toString().trim())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.e("SignUpViewModel", "Succeeded: ${task.exception}")
                    val user = FirebaseAuth.getInstance().currentUser
                    user?.uid?.let { userId ->
                        val db = FirebaseFirestore.getInstance()
                        val userDocument = db.collection("profiles").document(userId)
                        val userData = Profile(username, firstname, lastname, true, "user") // Creating a Profile object
                        userDocument.set(userData)
                            .addOnSuccessListener {
                                Log.e("SignUpViewModel", "User profile created")
                                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener { signInTask ->
                                        if (signInTask.isSuccessful) {
                                            Log.e("SignUpViewModel", "User logged in after registration")
                                            _loginSuccess.value = true // Indicates successful login
                                        } else {
                                            Log.e("SignUpViewModel", "Login after registration failed: ${signInTask.exception}")
                                        }
                                    }
                            }
                            .addOnFailureListener {
                                Log.e("SignUpViewModel", "Error creating user profile")
                            }
                    }
                } else {
                    Log.e("SignUpViewModel", "Sign up failed: ${task.exception}")
                }
            }
    }
}
