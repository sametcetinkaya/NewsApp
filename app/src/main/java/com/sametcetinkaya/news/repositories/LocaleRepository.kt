package com.sametcetinkaya.news.repositories

import com.sametcetinkaya.news.db.ArticleDao
import com.sametcetinkaya.news.model.Article
import javax.inject.Inject

class LocaleRepository @Inject constructor(
    val db: ArticleDao
) : BaseRepository() {

    suspend fun insertNewsToDb(article: Article) = db.insertAll(article)

    suspend fun deleteNews(article: Article) = db.deleteArticle(article)

    fun getSavedNews() = db.getAllArticles()
}