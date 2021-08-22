package com.nsantos.news_topheadlines.ui.fragments.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nsantos.news_topheadlines.getNewsArticleModel
import com.nsantos.news_topheadlines.getOrAwaitValue
import com.nsantos.news_topheadlines.network.toDatabaseModel
import com.nsantos.news_topheadlines.repository.FakeNewsRepository
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NewsArticlesViewModelTest{

    private lateinit var newsArticlesViewModel: NewsArticlesViewModel
    private lateinit var newsRepository: FakeNewsRepository

    @Before
    fun setup() {
        newsRepository = FakeNewsRepository()
        newsArticlesViewModel = NewsArticlesViewModel(savedStateHandle = SavedStateHandle(),newsRepository)
    }

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun getNewsArticles_NoNewsArticlesOnDatabase_returnEmpty(){

        val items = newsArticlesViewModel.newsArticlesList

        assertThat(items.getOrAwaitValue().data, `is` (emptyList()))
    }

    @Test
    fun getNewsArticles_insertArticlesOnDatabaseFromNetwork_returnArticles(){

        runBlocking {
            val items = newsArticlesViewModel.newsArticlesList

            val fetchNewsArticles = newsRepository.fetchNewsArticles().toDatabaseModel()
            newsRepository.deleteAllNewsArticles()
            newsRepository.insertAllNewsArticles(fetchNewsArticles)

            assertThat(items.getOrAwaitValue().data, `is` (fetchNewsArticles.toList()))
        }
    }

    @Test
    fun navigateToArticleDetail_returnNewsArticle(){

        val newsArticleModel = getNewsArticleModel()

        newsArticlesViewModel.navigateToArticleDetail(newsArticleModel)

        assertThat(newsArticlesViewModel.newsArticleModel.getOrAwaitValue(), `is`(newsArticleModel))
    }

    @Test
    fun navigateToArticleDetailFinished_returnNull(){

        newsArticlesViewModel.onNavigateToArticleDetailFinished()

        assertThat(newsArticlesViewModel.newsArticleModel.getOrAwaitValue(), nullValue())
    }

}