package com.example.shows_kolegakolega

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shows_kolegakolega.data.DemoData
import com.example.shows_kolegakolega.model.Review

class ShowDetailsViewModel : ViewModel() {

    private var id : String = "0"

    private val reviewsLiveData : MutableLiveData<List<Review>> by lazy {
        MutableLiveData<List<Review>>()
    }

    fun getReviewsLiveData() : LiveData<List<Review>> {
        return reviewsLiveData
    }

    fun initReviews(showId: String) {
        id = showId
        reviewsLiveData.value = DemoData.getShowById(id).reviews
    }

    fun addReview(review: Review){
        DemoData.addReview(review,id)
        reviewsLiveData.value = DemoData.getShowById(id).reviews
    }

    fun countReviews(): Int {
        return DemoData.getShowById(id).reviews.count()
    }

    fun getAverage(): Float{
        var count: Int = 0
        DemoData.getShowById(id).reviews.forEach{
            count+=it.rating
        }

        return count / countReviews().toFloat()
    }

}