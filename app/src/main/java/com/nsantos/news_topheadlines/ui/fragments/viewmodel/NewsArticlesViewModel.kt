package com.nsantos.news_topheadlines.ui.fragments.viewmodel

import androidx.lifecycle.*
import com.nsantos.news_topheadlines.models.NewsArticleModel
import com.nsantos.news_topheadlines.repository.INewsRepository
import kotlinx.coroutines.launch

class NewsArticlesViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val repository: INewsRepository
): ViewModel() {

    private val AUTHENTICATION_RESULT_KEY = "AUTHENTICATION_RESULT"

    val newsArticlesList = repository.getNewsArticles().asLiveData()

    private val _newsArticleModel = MutableLiveData<NewsArticleModel?>()

    val newsArticleModel: LiveData<NewsArticleModel?> = _newsArticleModel

    fun navigateToArticleDetail(newsArticleModel: NewsArticleModel){
        _newsArticleModel.value = newsArticleModel
    }

    fun onNavigateToArticleDetailFinished(){
        _newsArticleModel.value = null
    }

    fun saveBiometricAuthenticationResult(result: Boolean){
        savedStateHandle[AUTHENTICATION_RESULT_KEY] = result
    }

    fun getBiometricAuthenticationResult(): Boolean {
        return savedStateHandle.get<Boolean>(AUTHENTICATION_RESULT_KEY) ?: false
    }
}