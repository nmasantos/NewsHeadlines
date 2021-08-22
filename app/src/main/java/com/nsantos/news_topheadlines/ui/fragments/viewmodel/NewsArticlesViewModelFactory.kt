package com.nsantos.news_topheadlines.ui.fragments.viewmodel

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.nsantos.news_topheadlines.repository.NewsRepository

class NewsArticlesViewModelFactory(private val savedState: SavedStateRegistryOwner,
                                   private val newsRepository: NewsRepository)
         : AbstractSavedStateViewModelFactory(savedState, null) {

    override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, state: SavedStateHandle) =
            NewsArticlesViewModel(state, newsRepository) as T

}