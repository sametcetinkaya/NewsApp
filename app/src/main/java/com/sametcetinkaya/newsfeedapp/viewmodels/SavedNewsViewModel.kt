package com.sametcetinkaya.newsfeedapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.sametcetinkaya.newsfeedapp.models.Article
import com.sametcetinkaya.newsfeedapp.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedNewsViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {

    val listSavedNews = repository.getAllArticles().asLiveData()

    fun insertArticle(article: Article) = viewModelScope.launch {
        repository.insertArticle(article)
    }

    fun deleteArticle(article: Article) = viewModelScope.launch {
        repository.deleteArticle(article)
    }


}