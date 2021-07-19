package com.example.shows_kolegakolega

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shows_kolegakolega.databinding.ActivityShowDetailsBinding
import com.example.shows_kolegakolega.model.Show

class ShowDetailsActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_SHOW_NAME : String = "EXTRA_SHOW_NAME"
        private const val EXTRA_SHOW_DES : String = "EXTRA_SHOW_DES"
        private const val EXTRA_SHOW_IMAGE : String = "EXTRA_SHOW_IMAGE"

        fun buildIntent(activity : Activity, show : Show) : Intent {
            val intent = Intent(activity, ShowDetailsActivity::class.java)
            intent.putExtra(EXTRA_SHOW_NAME, show.name)
            intent.putExtra(EXTRA_SHOW_DES, show.description)
            intent.putExtra(EXTRA_SHOW_IMAGE, show.image.toString())
            return intent
        }

    }

    private lateinit var binding: ActivityShowDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = ActivityShowDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initLayout()
        intBackButton()
    }

    private fun intBackButton() {
        binding.toolbarBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initLayout() {
        binding.showTitle.text = intent.extras?.getString(EXTRA_SHOW_NAME)
        binding.showDescription.text = intent.extras?.getString(EXTRA_SHOW_DES)
        val img = intent.extras?.getString(EXTRA_SHOW_IMAGE)
        if (img != null) {
            binding.showImage.setImageResource( img.toInt() )
        }
    }
}