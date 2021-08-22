package com.nsantos.news_topheadlines.utils.repositoryutils

sealed class Resource<T>(val data: T?, val throwable: Throwable? = null) {

    class Success<T>(data: T): Resource<T>(data)
    class Loading<T>(data: T? = null): Resource<T>(data)
    class Error<T>(data: T? = null, throwable: Throwable?): Resource<T>(data, throwable)
}