package com.sametcetinkaya.newsfeedapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "article_db")
data class Article(
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,
    val title: String?,
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val url: String?,
    val urlToImage: String?
):Serializable
