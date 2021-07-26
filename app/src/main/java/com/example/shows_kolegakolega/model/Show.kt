package com.example.shows_kolegakolega.model

import androidx.annotation.DrawableRes

data class Show(
    val id : String,
    val name : String,
    val description : String,
    @DrawableRes val image : Int
)

