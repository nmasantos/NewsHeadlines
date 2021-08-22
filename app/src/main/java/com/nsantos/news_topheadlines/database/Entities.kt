package com.nsantos.news_topheadlines.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.nsantos.news_topheadlines.models.NewsArticleModel
import com.nsantos.news_topheadlines.utils.getCurrentDateTimeFormatted
import com.nsantos.news_topheadlines.utils.getFormattedDate

@Entity(tableName = "newsArticles")
data class NewsArticle(
    val author: String?,
    val sourceId: String,
    val sourceName: String,
    @PrimaryKey
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?,
    val createdDate: String = getCurrentDateTimeFormatted()
)

fun List<NewsArticle>.asModel(): List<NewsArticleModel> {
    return map {
        NewsArticleModel(
            author = it.author,
            sourceId = it.sourceId,
            sourceName = it.sourceName,
            title = it.title,
            description = it.description,
            url = it.url,
            urlToImage = it.urlToImage,
            publishedAt = getFormattedDate(it.publishedAt),
            content = it.content
        )
    }
}