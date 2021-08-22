package com.nsantos.news_topheadlines.network

import com.nsantos.news_topheadlines.database.NewsArticle

data class NewsApiResponse(val status: String, val totalResults: Int, val articles: List<NetworkNewsArticles>)

data class NetworkNewsArticles(
    val source: Source,
    val author: String?,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?
)

data class Source (val id: String, val name: String)

fun List<NetworkNewsArticles>.toDatabaseModel(): Array<NewsArticle> {
    return map {
        NewsArticle (
            sourceId = it.source.id,
            sourceName = it.source.name,
            author = it.author,
            title = it.title,
            description = it.description,
            url = it.url,
            urlToImage = it.urlToImage,
            publishedAt = it.publishedAt,
            content = it.content
        )
    }.toTypedArray()
}