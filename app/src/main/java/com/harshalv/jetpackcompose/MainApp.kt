package com.harshalv.jetpackcompose

import android.app.Application
import com.harshalv.jetpackcompose.data.Repository
import com.harshalv.jetpackcompose.data.network.Api
import com.harshalv.jetpackcompose.data.network.NewsManager

class MainApp : Application() {

    private val manager by lazy {
        NewsManager(Api.retrofitService)
    }

    val repository by lazy {
        Repository(manager = manager)
    }
}