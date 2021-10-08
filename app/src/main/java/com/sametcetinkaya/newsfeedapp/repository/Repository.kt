package com.sametcetinkaya.newsfeedapp.repository

import com.sametcetinkaya.newsfeedapp.api.NewsApi
import com.sametcetinkaya.newsfeedapp.db.ArticleDao
import com.sametcetinkaya.newsfeedapp.models.Article
import javax.inject.Inject

class Repository @Inject constructor(
    private val api: NewsApi,
    private val dao: ArticleDao) {

    suspend fun getBreakingNews(page:Int)  = api.getHeadLines(page = page)

    suspend fun searchNews(query:String) = api.searchNews(query)


    suspend fun insertArticle(article: Article) = dao.insert(article)

    suspend fun deleteArticle(article: Article) = dao.delete(article)

    fun getAllArticles() = dao.getAllArticles()
}