package com.learn.cancerapp.data.articles.retrofit

import com.learn.cancerapp.BuildConfig
import com.learn.cancerapp.data.articles.response.ArticleResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("v2/top-headlines?country=id&category=health&apiKey=${BuildConfig.API_KEY_ARTICLE}")
    fun getArticles(): Call<ArticleResponse>
}