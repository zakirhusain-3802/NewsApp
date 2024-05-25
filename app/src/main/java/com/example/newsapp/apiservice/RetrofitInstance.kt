package com.example.newsapp.apiservice

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    private const val BASE_URL = "https://newsapi.org/v2/"

    val client = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.MINUTES)
        .build()

    fun provideAPI() =
        Retrofit.Builder().client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(NewsApiService::class.java)

//    fun provideAPI() =
//        Retrofit.Builder().client(client)
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build().create(NewsApiService::class.java)


//    val api: NewsApiService = provideAPI().create(NewsApiService::class.java

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val api: NewsApiService = retrofit.create(NewsApiService::class.java)

}

