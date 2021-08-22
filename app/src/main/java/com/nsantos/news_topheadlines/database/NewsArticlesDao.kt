package com.nsantos.news_topheadlines.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsArticlesDao {
    @Query("SELECT * FROM newsArticles ORDER BY publishedAt DESC")
    fun getAllNewsArticles(): Flow<List<NewsArticle>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllNewsArticles(vararg newsArticles: NewsArticle)

    @Query ("DELETE FROM newsArticles")
    suspend fun deleteNewsArticles()
}