package com.alterok.mausamlive.core.data.remote

import android.content.SharedPreferences
import android.util.Log
import com.alterok.mausamlive.core.constant.AppConstants
import javax.inject.Inject

private const val TAG = "ApiKeyManager"
class ApiKeyManager @Inject constructor(private val sharedPreferences: SharedPreferences) {

    fun saveApiKey(apiKey: String) {
        Log.i(TAG, "saveApiKey: $apiKey")
        sharedPreferences.edit().putString(AppConstants.TAG_PREF_STR_API_KEY, apiKey).apply()
    }

    fun getApiKey() = sharedPreferences.getString(AppConstants.TAG_PREF_STR_API_KEY, null).apply {
        Log.i(TAG, "getApiKey: $this")
    }

    fun clearApiKey() = sharedPreferences.edit().putString(AppConstants.TAG_PREF_STR_API_KEY, null)
}