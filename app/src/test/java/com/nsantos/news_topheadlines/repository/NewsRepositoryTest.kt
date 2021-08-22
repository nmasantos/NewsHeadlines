package com.nsantos.news_topheadlines.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import com.nsantos.news_topheadlines.createNewsArticlesList
import com.nsantos.news_topheadlines.database.NewsArticlesDatabase
import com.nsantos.news_topheadlines.getOrAwaitValue
import com.nsantos.news_topheadlines.network.NewsApiService
import com.nsantos.news_topheadlines.utils.repositoryutils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class NewsRepositoryTest{

    //TODO
}