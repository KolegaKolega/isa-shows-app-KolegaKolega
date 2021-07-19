package com.example.shows_kolegakolega

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shows_kolegakolega.databinding.ActivityEmptyStateBinding

class EmptyStateActivity : AppCompatActivity() {

    companion object {
        fun buildIntent(activity: Activity): Intent {
            return Intent(activity, EmptyStateActivity::class.java)
        }

    }

    private lateinit var binding: ActivityEmptyStateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = ActivityEmptyStateBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.hideEmpty.setOnClickListener {
            val intent = ShowsActivity.buildIntent(this)
            startActivity(intent)
        }
    }
}