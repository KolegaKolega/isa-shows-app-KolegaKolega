package com.example.shows_kolegakolega

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shows_kolegakolega.data.FileUtil
import com.example.shows_kolegakolega.model.Show
import com.example.shows_kolegakolega.model.ShowsResponse
import com.example.shows_kolegakolega.model.UpdateImageResponse
import com.example.shows_kolegakolega.model.User
import com.example.shows_kolegakolega.networking.ApiModule
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ShowsViewModel : ViewModel() {

    private val showsLiveData : MutableLiveData<List<Show>> by lazy {
        MutableLiveData<List<Show>>()
    }

    private val userLiveData : MutableLiveData<User> by lazy {
        MutableLiveData<User>()
    }

    fun getUserLiveData(): LiveData<User> {
        return userLiveData
    }

    fun getShowsLiveData() : LiveData<List<Show>> {
        return showsLiveData
    }

    fun getShows(){
        ApiModule.retrofit.getShows().enqueue(object : Callback<ShowsResponse>{
            override fun onResponse(call: Call<ShowsResponse>, response: Response<ShowsResponse>) {
                showsLiveData.value = response.body()?.shows
            }

            override fun onFailure(call: Call<ShowsResponse>, t: Throwable) {
                showsLiveData.value = emptyList()
            }

        })
    }

    fun updateImage(id: Int, email: String, imageFile: File){
        val userId = id.toString().toRequestBody("multipart/from-data".toMediaTypeOrNull())
        val userEmail = email.toRequestBody("multipart/from-data".toMediaTypeOrNull())
        var profilePic: MultipartBody.Part?
        val request = imageFile.asRequestBody("multipart/from-data".toMediaTypeOrNull())
        profilePic = MultipartBody.Part.createFormData("image", imageFile.name, request)
        ApiModule.retrofit.updateImage(userId, userEmail, profilePic).enqueue(object : Callback<UpdateImageResponse>{
            override fun onResponse(
                call: Call<UpdateImageResponse>,
                response: Response<UpdateImageResponse>
            ) {
                userLiveData.value = response.body()?.user
            }

            override fun onFailure(call: Call<UpdateImageResponse>, t: Throwable) {
                userLiveData.value = null
            }

        })
    }
}

