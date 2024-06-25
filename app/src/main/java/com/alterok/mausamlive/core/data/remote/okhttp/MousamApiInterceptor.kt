package com.alterok.mausamlive.core.data.remote.okhttp

import com.alterok.mausamlive.core.constant.NetworkConstants
import com.alterok.mausamlive.core.data.remote.ApiKeyManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class MousamApiInterceptor @Inject constructor(private val apiKeyManager: ApiKeyManager) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val apiKey = apiKeyManager.getApiKey()
            ?: return chain.proceed(chain.request()).newBuilder()
                .code(403)
                .message(NetworkConstants.MSG_API_KEY_NOT_FOUND)
                .build()

        val newRequest = chain.request().newBuilder()
            .addHeader(
                NetworkConstants.HEADER_TITLE_API_KEY,
                apiKey
            )
            .build()

        val response = chain.proceed(newRequest)
        if (response.code == 401)
            return response.newBuilder()
                .code(401)
                .message(NetworkConstants.MSG_UNAUTHROIZED_API_KEY_INVALID)
                .build()

        return response
    }
}
