package com.example.cleannotes.business.data.util

import com.example.cleannotes.business.data.cache.CacheConstant.CACHE_TIMEOUT
import com.example.cleannotes.business.data.cache.CacheErrors.CACHE_ERROR_TIMEOUT
import com.example.cleannotes.business.data.cache.CacheErrors.CACHE_ERROR_UNKNOWN
import com.example.cleannotes.business.data.cache.CacheResult
import com.example.cleannotes.business.data.network.ApiResult
import com.example.cleannotes.business.data.network.NetworkConstant.NETWORK_TIMEOUT
import com.example.cleannotes.business.data.network.NetworkErrors.NETWORK_ERROR_TIMEOUT
import com.example.cleannotes.business.data.network.NetworkErrors.NETWORK_ERROR_UNKNOWN
import com.example.cleannotes.business.data.util.GenericErrors.ERROR_UNKNOWN
import com.example.cleannotes.business.domain.util.cLog
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T?
): ApiResult<T?> {
    return withContext(dispatcher) {
        try {
            // throws TimeoutCancellationException
            withTimeout(NETWORK_TIMEOUT) {
                ApiResult.Success(apiCall.invoke())
            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            when (throwable) {
                is TimeoutCancellationException -> {
                    val code = 408 // timeout error code
                    ApiResult.GenericError(code, NETWORK_ERROR_TIMEOUT)
                }
                is IOException -> {
                    ApiResult.NetworkError
                }
                is HttpException -> {
                    val code = throwable.code()
                    val errorResponse = convertErrorBody(throwable)
                    cLog(errorResponse)
                    ApiResult.GenericError(
                        code,
                        errorResponse
                    )
                }
                else -> {
                    cLog(NETWORK_ERROR_UNKNOWN)
                    ApiResult.GenericError(
                        null,
                        NETWORK_ERROR_UNKNOWN
                    )
                }
            }
        }
    }
}

suspend fun <T> safeCacheCall(
    dispatcher: CoroutineDispatcher,
    cacheCall: suspend () -> T?
): CacheResult<T?> {
    return withContext(dispatcher) {
        try {
            // throws TimeoutCancellationException
            withTimeout(CACHE_TIMEOUT) {
                CacheResult.Success(cacheCall.invoke())
            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            when (throwable) {

                is TimeoutCancellationException -> {
                    CacheResult.GenericError(CACHE_ERROR_TIMEOUT)
                }
                else -> {
                    cLog(CACHE_ERROR_UNKNOWN)
                    CacheResult.GenericError(CACHE_ERROR_UNKNOWN)
                }
            }
        }
    }
}


private fun convertErrorBody(throwable: HttpException): String? {
    return try {
        throwable.response()?.errorBody()?.string()
    } catch (exception: Exception) {
        ERROR_UNKNOWN
    }
}