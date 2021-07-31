package com.example.shows_kolegakolega

import android.media.Rating
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shows_kolegakolega.database.ReviewEntity
import com.example.shows_kolegakolega.database.ShowEntity
import com.example.shows_kolegakolega.database.ShowsDatabase
import com.example.shows_kolegakolega.model.*
import com.example.shows_kolegakolega.networking.ApiModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

class ShowDetailsViewModel(
    private val showsDatabase: ShowsDatabase
) : ViewModel() {

    companion object{
        private const val TEMP_ID = "tempId"
    }

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

                Executors.newSingleThreadExecutor().execute {
                    response.body()?.reviews?.let {
                        showsDatabase.reviewDao().insertAllReviews(it.map {it2->
                            ReviewEntity(it2.id, it2.comment, it2.rating, it2.showId, it2.user)
                        })
                    }
                }

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

    fun getReviewsFromDatabase(): LiveData<List<ReviewEntity>> {
        return showsDatabase.reviewDao().getAllReviews()
    }

    fun getShowFromDatabase(showId: String): LiveData<ShowEntity> {
        return showsDatabase.showDao().getShow(showId)
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

    fun createReviewInDataBase(rating: Int, comment: String?, showId: Int, user: User){
        Executors.newSingleThreadExecutor().execute {
            showsDatabase.reviewDao().insertSingleReview(
                ReviewEntity(TEMP_ID,comment, rating, showId, user)
            )
        }
    }

    fun deleteReviewInDatabase(reviewEntity: ReviewEntity){
        Executors.newSingleThreadExecutor().execute {
            showsDatabase.reviewDao().deleteReview(reviewEntity)
        }
    }

}