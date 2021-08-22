package com.nsantos.news_topheadlines.repository

import android.text.format.DateUtils
import androidx.room.withTransaction
import com.nsantos.news_topheadlines.BuildConfig
import com.nsantos.news_topheadlines.database.NewsArticle
import com.nsantos.news_topheadlines.database.NewsArticlesDatabase
import com.nsantos.news_topheadlines.network.NetworkNewsArticles
import com.nsantos.news_topheadlines.network.NewsApiService
import com.nsantos.news_topheadlines.network.toDatabaseModel
import com.nsantos.news_topheadlines.utils.hasTimeExpired
import com.nsantos.news_topheadlines.utils.repositoryutils.networkBoundResource
import kotlinx.coroutines.flow.Flow

class NewsRepository(private val database: NewsArticlesDatabase, private val service: NewsApiService): INewsRepository {

    companion object {
        private const val API_KEY = BuildConfig.ApiKey
        private const val sources = BuildConfig.sources
        private const val TAG = "NewsRepository"
        private const val EXPIRE_TIME = DateUtils.MINUTE_IN_MILLIS*10
    }

    private val articlesDao = database.articlesDao

    override fun getNewsArticles() = networkBoundResource(
        query = {
            loadAllNewsArticles()
        },
        fetch = {
            fetchNewsArticles()
        },
        saveFetchResult = { newsArticles ->
            database.withTransaction {
                deleteAllNewsArticles()
                insertAllNewsArticles(newsArticles.toDatabaseModel())
            }
        },
        shouldFetch = {
            shouldFetch(it)
        }
    )

    override fun loadAllNewsArticles(): Flow<List<NewsArticle>> {
        return articlesDao.getAllNewsArticles()
    }

    override suspend fun fetchNewsArticles(): List<NetworkNewsArticles> {
        val response = service.getTopHeadlines(sources, API_KEY)

        return if(response.status == "ok")
            response.articles
        else {
            listOf()
        }
    }

    override suspend fun insertAllNewsArticles(newsArticles: Array<NewsArticle>) {
        articlesDao.insertAllNewsArticles(*newsArticles)
    }

    override suspend fun deleteAllNewsArticles() {
        articlesDao.deleteNewsArticles()
    }

    override fun shouldFetch(newsArticles: List<NewsArticle>): Boolean {
        return newsArticles.isEmpty() || hasTimeExpired(newsArticles.first().createdDate, EXPIRE_TIME)
                || newsArticles.first().sourceId != sources
    }
}