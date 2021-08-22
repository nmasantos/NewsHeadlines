package com.nsantos.news_topheadlines.utils

import android.text.format.DateUtils
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class TimeUtilsTest {

    @Test
    fun hasTimeExpired_currentTimeLessExpiration_returnsTrue() {
        val timeNow = Calendar.getInstance().time
        val expireTime = DateUtils.MINUTE_IN_MILLIS*10
        val timeDifference = timeNow.time - expireTime
        val publishedAt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(Date(timeDifference))

        val result = hasTimeExpired(publishedAt, expireTime)

        assertThat(result, `is`(true))
    }

    @Test
    fun hasTimeExpired_currentTime_returnsFalse() {
        val timeNow = Calendar.getInstance().time
        val publishedAtCurrentTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(timeNow)
        val expireTime = DateUtils.MINUTE_IN_MILLIS*10

        val result = hasTimeExpired(publishedAtCurrentTime, expireTime)

        assertThat(result, `is`(false))
    }
}