package com.example.newsapp.viewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.Repo.NewsRepository
import com.example.newsapp.dataclass.NewsResponse
import kotlinx.coroutines.launch

class NewsViewModel(private val newsRepository: NewsRepository) : ViewModel() {
    private val _newsData = MutableLiveData<NewsResponse?>()
    val newsData: LiveData<NewsResponse?> = _newsData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        fetchNews(" ")
    }

    fun searchNews(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = newsRepository.searchNews(query)
                if (response.isSuccessful) {
                    _newsData.value = response.body()
                } else {
                    _newsData.value = null
                }
            } catch (e: Exception) {
                _newsData.value = null
                // Handle error
            }
            _isLoading.value = false
        }
    }
    fun fetchNews(category:String){
        println("Response : Called $category")
        viewModelScope.launch {
            _isLoading.value = true
            try {
                println("Response : Called $category")

                val response = newsRepository.fetchNews(category)
                println("Response : ${response.code()}")
                if (response.isSuccessful) {
                    _newsData.value = response.body()
                } else {
                    _newsData.value = null
                }
            } catch (e: Exception) {
                _newsData.value = null
                // Handle error
            }
            _isLoading.value = false
        }
    }
}