package com.example.shows_kolegakolega

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.example.shows_kolegakolega.databinding.ActivityWelcomBinding

class WelcomeActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_EMAIL : String = "EXTRA_EMAIL"

        fun buildIntent(activity : Activity, email : String) : Intent{
            val intent = Intent(activity, WelcomeActivity::class.java)
            intent.putExtra(EXTRA_EMAIL, email)
            return intent
        }

    }

    private lateinit var binding : ActivityWelcomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = ActivityWelcomBinding.inflate(layoutInflater)

        setContentView(binding.root)

        var email = intent.extras?.getString(EXTRA_EMAIL)

        email = email?.substring(0 , email.indexOf("@"))

        binding.welcomtext.text = "Welcome, $email"

    }
}