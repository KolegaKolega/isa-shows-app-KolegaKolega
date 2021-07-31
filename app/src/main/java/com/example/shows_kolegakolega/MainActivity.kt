package com.example.shows_kolegakolega

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.shows_kolegakolega.databinding.ActivityMainBinding
import com.example.shows_kolegakolega.networking.ApiModule
import com.example.shows_kolegakolega.networking.NetworkChecker


class MainActivity : AppCompatActivity() {
    companion object{
        private const val CONNECTION = "CONNECTION"
    }

    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ApiModule.initRetrofit(getPreferences(Context.MODE_PRIVATE))

    }
}