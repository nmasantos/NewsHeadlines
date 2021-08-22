package com.nsantos.news_topheadlines.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewsArticleModel(
    val sourceId: String,
    val sourceName: String,
    val author: String?,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?
    ) : Parcelable

