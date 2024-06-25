package com.alterok.mausamlive.core

import android.app.Application
import com.alterok.kotlin_essential_extensions.ifNull
import com.alterok.mausamlive.R
import com.alterok.mausamlive.core.data.remote.ApiKeyManager
import com.google.android.material.color.DynamicColors
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MousamLiveApplication : Application() {
    @Inject
    lateinit var apiKeyManager: ApiKeyManager
    override fun onCreate() {
        super.onCreate()

        DynamicColors.applyToActivitiesIfAvailable(this)

        apiKeyManager.getApiKey().ifNull {
            // TODO: refer to "api_key_debug" resource in strings.xml 
            apiKeyManager.saveApiKey(getString(R.string.api_key_release))
        }
    }
}