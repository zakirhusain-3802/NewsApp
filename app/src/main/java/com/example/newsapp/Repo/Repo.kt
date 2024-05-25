package com.example.newsapp.Repo

import com.example.newsapp.apiservice.NewsApiService
import com.example.newsapp.apiservice.RetrofitInstance
import com.example.newsapp.dataclass.NewsModel
import com.example.newsapp.dataclass.NewsResponse
import com.example.newsapp.viewModel.NewsViewModel
import retrofit2.Call
import retrofit2.Response

class NewsRepository(private val newsApiService: NewsApiService) {
    val apiKey = "689cd08885424782837d250b09774a83"

    suspend fun searchNews(query: String): Response<NewsResponse> {
        return newsApiService.searchNews(query, apiKey)
    }

    suspend  fun fetchNews(category: String): Response<NewsResponse> {

        return newsApiService.getTopHeadlines("us", category.toString(), apiKey)
    }

}