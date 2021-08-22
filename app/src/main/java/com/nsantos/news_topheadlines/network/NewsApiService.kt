package com.nsantos.news_topheadlines.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://newsapi.org/v2/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

val interceptor : HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
    this.level = HttpLoggingInterceptor.Level.BODY
}

val client : OkHttpClient = OkHttpClient.Builder().apply {
    this.addInterceptor(interceptor)
}.build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .client(client)
    .build()

interface NewsApiService{
    @GET("top-headlines")
    suspend fun getTopHeadlines(@Query("sources") sources: String, @Query("apiKey") apiKey: String): NewsApiResponse
}

object NewsApi{
    val retrofitService : NewsApiService by lazy { retrofit.create(NewsApiService::class.java) }
}