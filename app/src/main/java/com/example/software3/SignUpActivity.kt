package com.example.software3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.software3.databinding.ActivitySignUpBinding
import com.example.software3.viewmodel.SignUpViewModel

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)

        val viewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.loginSuccess.observe(this, { loginSuccess ->
            if (loginSuccess) {
                val intent = Intent(this, DashboardActivity::class.java) // Vervang DashboardActivity::class.java met de daadwerkelijke dashboard-activiteit
                startActivity(intent)
                finish() // Sluit de huidige SignUpActivity om niet terug te kunnen gaan
            }
        })
    }
}

