package com.nsantos.news_topheadlines.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [NewsArticle::class], version = 1)
abstract class NewsArticlesDatabase : RoomDatabase() {
    abstract val articlesDao: NewsArticlesDao
}

private lateinit var INSTANCE: NewsArticlesDatabase

fun getDatabase(context: Context): NewsArticlesDatabase {
    synchronized(NewsArticlesDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                NewsArticlesDatabase::class.java,
                "newsarticles").fallbackToDestructiveMigration().build()
        }
    }
    return INSTANCE
}
