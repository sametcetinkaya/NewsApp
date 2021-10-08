package com.sametcetinkaya.newsfeedapp.api

import com.sametcetinkaya.newsfeedapp.models.NewsResponse
import com.sametcetinkaya.newsfeedapp.utils.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("/v2/top-headlines")
    suspend fun getHeadLines(
        @Query("country") country:String = "tr",
        @Query("page") page:Int,
        @Query("apiKey") key:String = API_KEY
    ): Response<NewsResponse>


    @GET("/v2/everything")
    suspend fun searchNews(
        @Query("q") searchQuery:String,
        @Query("page") page:Int=1,
        @Query("apiKey") key:String = API_KEY
    ): Response<NewsResponse>
}