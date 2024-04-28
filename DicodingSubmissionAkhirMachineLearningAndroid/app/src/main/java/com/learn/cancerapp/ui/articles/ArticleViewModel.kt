package com.learn.cancerapp.ui.articles

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.learn.cancerapp.data.articles.response.ArticleResponse
import com.learn.cancerapp.data.articles.response.ArticlesItem
import com.learn.cancerapp.data.articles.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArticleViewModel : ViewModel() {
    private val _listArticles = MutableLiveData<List<ArticlesItem>>()
    val listReview: LiveData<List<ArticlesItem>> = _listArticles

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        findUsers()
    }

    private fun findUsers() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getArticles()
        client.enqueue(object : Callback<ArticleResponse> {
            override fun onResponse(
                call: Call<ArticleResponse>,
                response: Response<ArticleResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listArticles.value = response.body()?.articles
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ArticleResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}