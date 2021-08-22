package com.nsantos.news_topheadlines.repository

import com.nsantos.news_topheadlines.database.NewsArticle
import com.nsantos.news_topheadlines.network.NetworkNewsArticles
import com.nsantos.news_topheadlines.utils.repositoryutils.Resource
import kotlinx.coroutines.flow.Flow

interface INewsRepository {

    fun getNewsArticles(): Flow<Resource<out List<NewsArticle>>>

    fun loadAllNewsArticles(): Flow<List<NewsArticle>>

    suspend fun fetchNewsArticles(): List<NetworkNewsArticles>

    suspend fun insertAllNewsArticles(newsArticles: Array<NewsArticle>)

    suspend fun deleteAllNewsArticles()

    fun shouldFetch(newsArticles: List<NewsArticle>): Boolean
}