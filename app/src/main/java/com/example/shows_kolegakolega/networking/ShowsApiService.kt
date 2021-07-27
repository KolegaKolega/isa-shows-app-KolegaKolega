package com.example.shows_kolegakolega.networking

import com.example.shows_kolegakolega.model.RegisterRequest
import com.example.shows_kolegakolega.model.RegisterResponse
import com.example.shows_kolegakolega.model.SignInRequest
import com.example.shows_kolegakolega.model.SignInResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ShowsApiService {

    @POST("/users")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("/users/sign_in")
    fun signin(@Body request: SignInRequest): Call<SignInResponse>

}