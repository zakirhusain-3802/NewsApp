package com.example.newsapp.dataclass

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)
data class NewsModel(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)



