package com.example.shows_kolegakolega

import android.media.Rating
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shows_kolegakolega.model.*
import com.example.shows_kolegakolega.networking.ApiModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowDetailsViewModel : ViewModel() {

    private var id : String = "0"

    private val reviewsLiveData : MutableLiveData<List<Review>> by lazy {
        MutableLiveData<List<Review>>()
    }

    private val singleShowLiveData: MutableLiveData<Show> by lazy {
        MutableLiveData<Show>()
    }

    private val createReviewResultLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    fun getCreateReviewResultLiveData(): LiveData<Boolean> {
        return createReviewResultLiveData
    }

    fun getReviewsLiveData() : LiveData<List<Review>> {
        return reviewsLiveData
    }

    fun getSingleShowLiveData(): LiveData<Show>{
        return singleShowLiveData
    }

    fun getReviews(showId: Int){
        ApiModule.retrofit.getReviews(showId).enqueue(object: Callback<ReviewResponse>{
            override fun onResponse(
                call: Call<ReviewResponse>,
                response: Response<ReviewResponse>
            ) {
                reviewsLiveData.value = response.body()?.reviews
            }

            override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {
                reviewsLiveData.value = null
            }

        })
    }

    fun getShow(id: String){
        ApiModule.retrofit.getShow(id).enqueue(object : Callback<SingleShowResponse> {
            override fun onResponse(
                call: Call<SingleShowResponse>,
                response: Response<SingleShowResponse>
            ) {
                singleShowLiveData.value = response.body()?.show
            }

            override fun onFailure(call: Call<SingleShowResponse>, t: Throwable) {
                singleShowLiveData.value = null
            }

        })
    }

    fun createReview(rating: Int, comment: String?, showId: Int){
        ApiModule.retrofit.createReview(CreateReviewRequest(rating,comment,showId))
            .enqueue(object : Callback<CreateReviewResponse>{
                override fun onResponse(
                    call: Call<CreateReviewResponse>,
                    response: Response<CreateReviewResponse>
                ) {
                    createReviewResultLiveData.value = response.isSuccessful
                }

                override fun onFailure(call: Call<CreateReviewResponse>, t: Throwable) {
                    createReviewResultLiveData.value = false
                }

            })
    }

}