package com.alterok.mausamlive.core.domain.util

interface StaleDataChecker {
    fun isStale(lastTimeStamp: Long): Boolean
    fun updateRefreshInterval(refreshIntervalInMillis: Long)
}