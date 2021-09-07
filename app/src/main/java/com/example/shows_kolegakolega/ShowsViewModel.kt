package com.example.shows_kolegakolega

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shows_kolegakolega.data.DemoData
import com.example.shows_kolegakolega.model.Show

class ShowsViewModel : ViewModel() {

    private val shows = DemoData.shows

    private val showsLiveData : MutableLiveData<List<Show>> by lazy {
        MutableLiveData<List<Show>>()
    }

    fun getShowsLiveData() : LiveData<List<Show>> {
        return showsLiveData
    }

    fun initShows() {
        showsLiveData.value = shows
    }

}