package com.nsantos.news_topheadlines.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.nsantos.news_topheadlines.createNewsArticlesDatabaseList
import com.nsantos.news_topheadlines.getOrAwaitValueAndroidTest
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ArticlesDaoTest {

    private lateinit var newsArticlesDatabase: NewsArticlesDatabase

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initDatabase(){
        newsArticlesDatabase = Room.inMemoryDatabaseBuilder(getApplicationContext(),
            NewsArticlesDatabase::class.java).allowMainThreadQueries().build()
    }

    @Test
    fun insertArticlesAndRetrieveAll() = runBlocking {
        val articlesList = createNewsArticlesDatabaseList(10)

        newsArticlesDatabase.articlesDao.insertAllNewsArticles(*articlesList)

        val loadAllArticles = newsArticlesDatabase.articlesDao.getAllNewsArticles().asLiveData().getOrAwaitValueAndroidTest()
        val firstNewsArticle = loadAllArticles.first()

        assertThat(firstNewsArticle, notNullValue())
        assertThat(firstNewsArticle.title, `is` (articlesList.first().title))
        assertThat(firstNewsArticle.description, `is` (articlesList.first().description))
        assertThat(firstNewsArticle.publishedAt, `is` (articlesList.first().publishedAt))
    }

    @Test
    fun deleteAllArticlesAndRetrieveNone() = runBlocking {
        val articlesList = createNewsArticlesDatabaseList(10)

        newsArticlesDatabase.articlesDao.insertAllNewsArticles(*articlesList)
        newsArticlesDatabase.articlesDao.deleteNewsArticles()

        val loadAllArticles = newsArticlesDatabase.articlesDao.getAllNewsArticles().asLiveData().getOrAwaitValueAndroidTest()

        assertThat(loadAllArticles, `is` (emptyList()))
    }

    @After
    fun closeDb() = newsArticlesDatabase.close()
}