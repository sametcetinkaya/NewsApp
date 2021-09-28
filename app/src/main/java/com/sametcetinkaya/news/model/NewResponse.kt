package com.sametcetinkaya.news.model


data class NewResponse(

    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)


