package com.sametcetinkaya.news.repositories

import com.sametcetinkaya.news.model.Article
import com.sametcetinkaya.news.network.NewsApi
import javax.inject.Inject

class RemoteRepository @Inject constructor(
    private val newsApi: NewsApi
) : BaseRepository(){

    suspend fun getBreakingNews(): MutableList<Article>? {
        return safeApiCall(
            call = { newsApi.getNews() },
            error = "Error fetching news"
        )?.articles?.toMutableList()
    }

    suspend fun searchNews(searchQuery: String): MutableList<Article>? {
        return safeApiCall(
            call = { newsApi.searchForNews(searchQuery) },
            error = "Error fetching news"
        )?.articles?.toMutableList()
    }
}