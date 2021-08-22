package com.nsantos.news_topheadlines.utils

import android.text.format.DateUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun getFormattedDate(dateTimestamp: String): String{
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    try {
        val time: Long = sdf.parse(dateTimestamp).time
        val now = System.currentTimeMillis()
        return DateUtils.getRelativeTimeSpanString(time, now, DateUtils.HOUR_IN_MILLIS).toString()
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return dateTimestamp
}

fun getCurrentDateTimeFormatted(): String{
    val currentTime = Calendar.getInstance().time
    return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(currentTime)
}

fun hasTimeExpired(dateTimestamp: String, expireTime: Long): Boolean{
    var hasExpired = false

    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    try {
        val time: Long = sdf.parse(dateTimestamp).time
        val now = System.currentTimeMillis()
        val timeDifference = (now - time) - expireTime
        hasExpired = timeDifference > 0
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return hasExpired
}