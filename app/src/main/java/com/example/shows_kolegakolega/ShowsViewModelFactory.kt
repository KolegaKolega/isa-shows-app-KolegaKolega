package com.example.shows_kolegakolega

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shows_kolegakolega.database.ShowsDatabase
import java.lang.IllegalArgumentException

class ShowsViewModelFactory(private val showsDatabase: ShowsDatabase): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ShowsViewModel::class.java)){
            return ShowsViewModel(showsDatabase) as T
        }

        throw IllegalArgumentException("Sorry, factory doesn't work")
    }
}