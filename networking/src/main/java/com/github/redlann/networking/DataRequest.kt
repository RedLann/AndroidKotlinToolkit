package com.github.redlann.networking

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.coroutineScope

sealed class DataRequest<out S> {
    inline fun <T> fold(failed: (Exception) -> T, succeeded: (S) -> T): T =
        when (this) {
            is Failure -> failed(failure)
            is Success -> succeeded(success)
        }

    fun data(): S? {
        return if (this is Success) success else null
    }

    fun error(): Exception? {
        return if (this is Failure) failure else null
    }

    fun isSuccessful(): Boolean {
        return this is Success
    }

    fun isFailure(): Boolean {
        return this is Failure
    }
}

object None

data class Failure(val failure: Exception) : DataRequest<Nothing>()
data class Success<out S>(val success: S) : DataRequest<S>()

typealias EmptyResponse = DataRequest<None>
typealias DeferredEmptyResponse = Deferred<EmptyResponse>
typealias LiveDataResponse<T> = LiveData<DataRequest<T>>
typealias ListLiveDataResponse<T> = LiveData<DataRequest<List<T>>>

class EmptyResponseRequest(val block: suspend () -> Unit) {
    suspend fun run(): EmptyResponse = coroutineScope {
        try {
            block()
            Success(None)
        } catch (e : Exception) {
            Failure(e)
        }
    }
}