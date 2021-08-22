package com.nsantos.news_topheadlines

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.nsantos.news_topheadlines.database.NewsArticle
import com.nsantos.news_topheadlines.models.NewsArticleModel
import com.nsantos.news_topheadlines.network.NetworkNewsArticles
import com.nsantos.news_topheadlines.network.Source
import kotlinx.coroutines.flow.flow
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import javax.annotation.Resource

fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }

    this.observeForever(observer)

    // Don't wait indefinitely if the LiveData is not set.
    if (!latch.await(time, timeUnit)) {
        throw TimeoutException("LiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}

fun getNewsArticleModel() =
    NewsArticleModel (
        sourceId = "bbc-news",
        sourceName = "BBC News",
        author =  "BBC News",
        title = "News article title",
        description =  "News article description",
        url = "http://www.bbc.co.uk",
        urlToImage =  "https://ichef.bbci.co.uk/news/1024/",
        publishedAt =  "2021-08-15T23:52:22.6515016Z",
        content =  "News article content"
    )

fun getNewsArticle() =
    NewsArticle (
        sourceId = "bbc-news",
        sourceName = "BBC News",
        author =  "BBC News",
        title = "News article title",
        description =  "News article description",
        url = "http://www.bbc.co.uk",
        urlToImage =  "https://ichef.bbci.co.uk/news/1024/",
        publishedAt =  "2021-08-15T23:52:22.6515016Z",
        content =  "News article content"
    )

fun getNewsArticleNetwork() =
    NetworkNewsArticles (
        source = Source("bbc-news", "BBC-News"),
        author =  "BBC News",
        title = "News article title",
        description =  "News article description",
        url = "http://www.bbc.co.uk",
        urlToImage =  "https://ichef.bbci.co.uk/news/1024/",
        publishedAt =  "2021-08-15T23:52:22.6515016Z",
        content =  "News article content"
    )

fun createNewsArticlesList(numberOfElements: Int): List<NewsArticle> {
    val newsArticleList = mutableListOf<NewsArticle>()
    repeat(numberOfElements){
        newsArticleList.add(getNewsArticle())
    }
    return newsArticleList.toList()
}

fun createNewsArticlesNetworkList(numberOfElements: Int): List<NetworkNewsArticles> {
    val newsArticleList = mutableListOf<NetworkNewsArticles>()
    repeat(numberOfElements){
        newsArticleList.add(getNewsArticleNetwork())
    }
    return newsArticleList.toList()
}



