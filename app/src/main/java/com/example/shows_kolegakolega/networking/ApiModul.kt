package com.example.shows_kolegakolega.networking

import android.content.SharedPreferences
import android.os.Bundle
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

object ApiModule {


    private const val ACCESS_TOKEN = "access-token"
    private const val CLIENT = "client"
    private const val UID = "uid"

    private const val BASE_URL = "https://tv-shows.infinum.academy"

    lateinit var retrofit: ShowsApiService

    fun initRetrofit(preferences: SharedPreferences) {
        val okhttp = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor {
                var access_token: String?
                var client: String?
                var uid: String?
                with(preferences){
                    access_token = getString(ACCESS_TOKEN, null)
                    client = getString(CLIENT, null)
                    uid = getString(UID,null)
                }
                val request = it.request()
                var newRequest = request.newBuilder()
                if(access_token != null) 
                    newRequest.addHeader(ACCESS_TOKEN, access_token!!)
                if(access_token != null)
                    newRequest.addHeader(CLIENT, client!!)
                if(access_token != null)
                    newRequest.addHeader(UID, uid!!)
                
                it.proceed(newRequest.build())
            }
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(Json{
                ignoreUnknownKeys = true
            }.asConverterFactory("application/json".toMediaType()))
            .client(okhttp)
            .build()
            .create(ShowsApiService::class.java)
    }
}