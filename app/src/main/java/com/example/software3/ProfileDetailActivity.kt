package com.example.software3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView

class ProfileDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_detail)

        supportActionBar?.apply {
            title = "Profiel Details"
            setDisplayHomeAsUpEnabled(true)
        }


        val usernameTextView: TextView = findViewById(R.id.detailUsernameTextView)
        val firstNameTextView: TextView = findViewById(R.id.detailFirstNameTextView)
        val lastNameTextView: TextView = findViewById(R.id.detailLastNameTextView)
        val roleTextView: TextView = findViewById(R.id.detailRoleTextView)

        val username = intent.getStringExtra("USERNAME")
        val firstName = intent.getStringExtra("FIRST_NAME")
        val lastName = intent.getStringExtra("LAST_NAME")
        val role = intent.getStringExtra("ROLE")

        usernameTextView.text = username
        firstNameTextView.text = firstName
        lastNameTextView.text = lastName
        roleTextView.text = role
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
