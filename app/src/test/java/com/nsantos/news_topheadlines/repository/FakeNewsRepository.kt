package com.nsantos.news_topheadlines.repository

import com.nsantos.news_topheadlines.createNewsArticlesNetworkList
import com.nsantos.news_topheadlines.database.NewsArticle
import com.nsantos.news_topheadlines.network.NetworkNewsArticles
import com.nsantos.news_topheadlines.utils.repositoryutils.Resource
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map


class FakeNewsRepository: INewsRepository {

    private val newsArticlesListDatabase = mutableListOf<NewsArticle>()
    private val newsArticlesListService = createNewsArticlesNetworkList(10)

    var shouldFetchFromService = false

    override fun getNewsArticles() = flow {
        val flow = loadAllNewsArticles().map { Resource.Success(it) }
        emitAll(flow)
    }

    override fun loadAllNewsArticles() = flow {
            this.emit(newsArticlesListDatabase.toList())
    }

    override suspend fun fetchNewsArticles(): List<NetworkNewsArticles> {
        return newsArticlesListService
    }

    override suspend fun insertAllNewsArticles(newsArticles: Array<NewsArticle>) {
        for (newsArticle in newsArticles)
            newsArticlesListDatabase.add(newsArticle)
    }

    override suspend fun deleteAllNewsArticles() {
        newsArticlesListDatabase.clear()
    }

    override fun shouldFetch(newsArticles: List<NewsArticle>): Boolean {
        return shouldFetchFromService
    }
}