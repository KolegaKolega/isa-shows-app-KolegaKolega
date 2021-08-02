package com.example.shows_kolegakolega.custom_view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.shows_kolegakolega.R
import com.example.shows_kolegakolega.databinding.ItemReviwBinding

class ItemReviewView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): ConstraintLayout(context, attrs, defStyleAttr) {

    private var binding: ItemReviwBinding

    init {

        binding = ItemReviwBinding.inflate(LayoutInflater.from(context), this)
        val pixelPadding = context.resources.getDimensionPixelSize(R.dimen.card_padding)
        setPadding(pixelPadding)

        clipToPadding = false

    }

    fun setEmail(email: String){
        binding.name.text = email
    }

    fun setRating(rating: Int){
        binding.rating.text = rating.toString()
    }

    fun setComment(comment: String){
        binding.reviweComment.text = comment
    }

    fun setCommentVisible(visibility: Boolean){
        binding.reviweComment.isVisible = visibility
    }

    fun setProfilePicture(imageUrl: String){
        Glide.with(this)
            .load(imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(binding.placeholder)
    }
}