package com.sametcetinkaya.news.network


import com.sametcetinkaya.news.model.NewResponse
import com.sametcetinkaya.news.utils.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("top-headlines")
     suspend fun getNews(
        @Query("country")
        countryCode: String = "tr",
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<NewResponse>

    @GET("everything")
    suspend fun searchForNews(
        @Query("q")
        searchQuery: String,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<NewResponse>

}