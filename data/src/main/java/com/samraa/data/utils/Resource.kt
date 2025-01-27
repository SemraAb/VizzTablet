package com.samraa.data.utils


sealed class Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>()

    data class Error(
        val message: String? = "",
        val statusEnum: ResponseStatus = ResponseStatus.FAILED,
        var statusCode: Int = 0
    ) : Resource<Nothing>()
}

inline fun <T : Any> Resource<T>.   onSuccess(action: (T) -> Unit): Resource<T> {
    if (this is Resource.Success) action(data)
    return this
}

inline fun <T : Any> Resource<T>.onError(action: (message: String?, statusEnum: ResponseStatus, statusCode: Int) -> Unit): Resource<T> {
    if (this is Resource.Error) action(message, statusEnum, statusCode)
    return this
}