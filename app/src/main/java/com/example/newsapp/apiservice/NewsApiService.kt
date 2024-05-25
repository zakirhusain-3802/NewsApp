package com.example.newsapp.apiservice

import com.example.newsapp.dataclass.NewsModel
import com.example.newsapp.dataclass.NewsResponse
import com.example.newsapp.viewModel.NewsViewModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String,
        @Query("category") category: String,
        @Query("apiKey") apiKey: String
    ): Response<NewsResponse>

    @GET("everything")
    suspend fun searchNews(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String = "Your API Key"
    ): Response<NewsResponse>
}
