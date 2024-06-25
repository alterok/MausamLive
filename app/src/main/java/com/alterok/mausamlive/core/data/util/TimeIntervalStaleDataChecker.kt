package com.alterok.mausamlive.core.data.util

import android.text.format.DateUtils
import com.alterok.mausamlive.core.domain.util.StaleDataChecker

class TimeIntervalStaleDataChecker : StaleDataChecker {
    private var refreshInterval: Long = DateUtils.MINUTE_IN_MILLIS * 3

    override fun isStale(lastTimeStamp: Long): Boolean {
        return (refreshInterval + lastTimeStamp) <= System.currentTimeMillis()
    }

    override fun updateRefreshInterval(refreshIntervalInMillis: Long) {
        this.refreshInterval = refreshIntervalInMillis
    }
}