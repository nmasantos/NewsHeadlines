package com.nsantos.news_topheadlines

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.nsantos.news_topheadlines.database.NewsArticle
import com.nsantos.news_topheadlines.network.NetworkNewsArticles
import com.nsantos.news_topheadlines.network.Source
import com.nsantos.news_topheadlines.network.toDatabaseModel
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

fun <T> LiveData<T>.getOrAwaitValueAndroidTest(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValueAndroidTest.removeObserver(this)
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

fun getNewsArticleNetworkUtil() =
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

fun createNewsArticlesDatabaseList(numberOfElements: Int): Array<NewsArticle> {
    val newsArticleList = mutableListOf<NetworkNewsArticles>()
    repeat(numberOfElements){
        newsArticleList.add(getNewsArticleNetworkUtil())
    }
    return newsArticleList.toDatabaseModel()
}



