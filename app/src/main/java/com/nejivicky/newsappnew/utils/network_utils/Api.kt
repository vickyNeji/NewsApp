package com.nejivicky.newsappnew.utils.network_utils

import com.nejivicky.newsappnew.models.NewsResponse
import com.nejivicky.newsappnew.utils.Constants
import com.nejivicky.newsappnew.utils.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")countryCode:String="us",
        @Query("page")pageNo:Int=1,
        @Query("apiKey")apiKey:String=API_KEY,
    ):Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q")searchQuery:String,
        @Query("page")pageNo:Int=1,
        @Query("apiKey")apiKey:String=API_KEY,
    ):Response<NewsResponse>


}