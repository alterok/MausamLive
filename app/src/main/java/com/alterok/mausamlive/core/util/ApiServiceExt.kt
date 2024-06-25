package com.alterok.mausamlive.core.util

import com.alterok.dataresult.DataResult
import com.alterok.dataresult.error.ExceptionResultError
import com.alterok.dataresult.error.NetworkResultError
import com.alterok.mausamlive.core.constant.NetworkConstants
import com.alterok.mausamlive.core.data.remote.ApiKeyNotFoundException
import com.alterok.mausamlive.core.data.remote.UnauthorizedKeyException
import retrofit2.Response

fun <T> Response<T>?.asDataResult(): DataResult<T> {
    if (this == null)
        return DataResult.Failure(NetworkResultError.fromCode(404))

    return if (isSuccessful && code() == 200) {
        val data = body()
        if (data == null)
            DataResult.Failure(NetworkResultError.NoContent)
        else
            DataResult.Success(data)
    } else {
        val dataError = when (message()) {
            NetworkConstants.MSG_API_KEY_NOT_FOUND -> ExceptionResultError.Custom(
                ApiKeyNotFoundException()
            )

            NetworkConstants.MSG_UNAUTHROIZED_API_KEY_INVALID -> ExceptionResultError.Custom(
                UnauthorizedKeyException()
            )

            else -> NetworkResultError.fromCode(code())
        }
        DataResult.Failure(dataError)
    }
}