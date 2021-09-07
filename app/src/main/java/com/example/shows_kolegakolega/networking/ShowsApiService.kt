package com.example.shows_kolegakolega.networking

import com.example.shows_kolegakolega.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ShowsApiService {

    @POST("/users")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("/users/sign_in")
    fun signIn(@Body request: SignInRequest): Call<SignInResponse>

    @GET("/shows")
    fun getShows(): Call<ShowsResponse>

    @GET("/shows/{id}")
    fun getShow(@Path("id") id: String): Call<SingleShowResponse>

    @GET("/shows/{show_id}/reviews")
    fun getReviews(@Path("show_id") showId: Int): Call<ReviewResponse>

    @POST("/reviews")
    fun createReview(@Body request: CreateReviewRequest): Call<CreateReviewResponse>

    @Multipart
    @PUT("/users")
    fun updateImage(@Part("id") id: RequestBody,
                    @Part("email") email: RequestBody,
                    @Part image : MultipartBody.Part): Call<UpdateImageResponse>
}