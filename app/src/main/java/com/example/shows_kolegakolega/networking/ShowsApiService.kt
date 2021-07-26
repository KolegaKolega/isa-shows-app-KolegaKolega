package com.example.shows_kolegakolega.networking

import com.example.shows_kolegakolega.model.RegisterRequest
import com.example.shows_kolegakolega.model.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ShowsApiService {

    @POST("/users")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>
}