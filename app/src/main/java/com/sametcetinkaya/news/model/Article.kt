package com.sametcetinkaya.news.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable


@Entity(tableName = "articles")

data class Article(


    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    val Id: Int? = null,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,
    val title: String,
    val url: String?,
    val urlToImage: String?,
):Serializable