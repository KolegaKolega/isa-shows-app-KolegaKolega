package com.example.shows_kolegakolega.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateImageResponse(
    @SerialName("user") val user: User
)
