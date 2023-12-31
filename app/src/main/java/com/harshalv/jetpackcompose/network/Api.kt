package com.harshalv.jetpackcompose.network

import com.harshalv.jetpackcompose.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Api {

    private val BASE_URL = "https://newsapi.org/v2/"

    private val httpLoggingInterceptor = HttpLoggingInterceptor().also {
        it.level = HttpLoggingInterceptor.Level.BODY
    }

    val okHttpClient = OkHttpClient.Builder().also {
        if (BuildConfig.DEBUG) it.addInterceptor(httpLoggingInterceptor)
    }.build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    val retrofitService: NewsService by lazy { retrofit.create(NewsService::class.java) }
}