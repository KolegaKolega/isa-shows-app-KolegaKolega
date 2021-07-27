package com.example.shows_kolegakolega

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shows_kolegakolega.model.SignInRequest
import com.example.shows_kolegakolega.model.SignInResponse
import com.example.shows_kolegakolega.networking.ApiModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInViewModel : ViewModel(){

    private val signInResultLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    fun getSignInResultLiveData(): LiveData<Boolean> {
        return signInResultLiveData
    }

    fun signIn(email: String, password: String){
        ApiModule.retrofit.signin(SignInRequest(email,password)).enqueue(object : Callback<SignInResponse> {
            override fun onResponse(
                call: Call<SignInResponse>,
                response: Response<SignInResponse>
            ) {
                signInResultLiveData.value = response.isSuccessful

            }

            override fun onFailure(call: Call<SignInResponse>, t: Throwable) {
                signInResultLiveData.value = false
            }

        })
    }
}