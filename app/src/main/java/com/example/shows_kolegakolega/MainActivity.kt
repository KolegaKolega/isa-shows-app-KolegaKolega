package com.example.shows_kolegakolega

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shows_kolegakolega.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }
}