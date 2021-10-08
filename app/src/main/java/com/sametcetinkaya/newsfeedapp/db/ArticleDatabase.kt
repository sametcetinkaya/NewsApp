package com.sametcetinkaya.newsfeedapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sametcetinkaya.newsfeedapp.models.Article
import com.sametcetinkaya.newsfeedapp.utils.Converters

@Database(
    entities = [Article::class],
    version = 3
)
@TypeConverters(Converters::class)
abstract class ArticleDatabase: RoomDatabase() {

    abstract fun getDao():ArticleDao
}