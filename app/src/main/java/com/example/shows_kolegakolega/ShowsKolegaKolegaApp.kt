package com.example.shows_kolegakolega

import android.app.Application
import android.content.Context
import com.example.shows_kolegakolega.database.ShowsDatabase
import com.example.shows_kolegakolega.networking.NetworkChecker
import java.util.concurrent.Executors

class ShowsKolegaKolegaApp : Application(){

    val showsDatabase by lazy{
        ShowsDatabase.getDatabase(this)
    }

    override fun onCreate() {
        super.onCreate()
        Executors.newSingleThreadExecutor().execute {
        }
    }
}