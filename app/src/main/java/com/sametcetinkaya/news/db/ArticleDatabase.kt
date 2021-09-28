package com.sametcetinkaya.news.db

import androidx.room.*
import com.sametcetinkaya.news.model.Article

@Database(
    entities = [Article::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase() {

    abstract fun getArticleDao(): ArticleDao

    }

