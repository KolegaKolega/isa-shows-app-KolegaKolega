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

    companion object {
        private const val ACCESS_TOKEN = "access-token"
        private const val CLIENT = "client"
        private const val UID = "uid"
        private const val EMAIL = "email"
        private const val ID = "id"
        private const val IMAGE = "image"

    }

    private val signInResultLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    fun getSignInResultLiveData(): LiveData<Boolean> {
        return signInResultLiveData
    }

    fun signIn(email: String, password: String, preferences: SharedPreferences){
        ApiModule.retrofit.signIn(SignInRequest(email,password)).enqueue(object : Callback<SignInResponse> {
            override fun onResponse(
                call: Call<SignInResponse>,
                response: Response<SignInResponse>
            ) {
                signInResultLiveData.value = response.isSuccessful
                with(preferences.edit()){
                    putString(ACCESS_TOKEN, response.headers()[ACCESS_TOKEN])
                    putString(CLIENT, response.headers()[CLIENT])
                    putString(UID, response.headers()[UID])
                    response.body()?.user?.let { putString(EMAIL, it.email) }
                    putString(IMAGE, response.body()?.user?.imageUrl)
                    response.body()?.user?.let { putInt(ID, it.id) }
                    apply()
                }

            }

            override fun onFailure(call: Call<SignInResponse>, t: Throwable) {
                signInResultLiveData.value = false
            }

        })
    }
}