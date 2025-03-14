package com.samraa.data.utils

import okhttp3.ResponseBody
import retrofit2.HttpException
import timber.log.Timber

object ResponseHandler {

    fun <T : Any> handleSuccess(data: T?): Resource<T> {
        return if (data != null)
            Resource.Success(data)
        else {
            Resource.Error()
        }
    }

    fun <T : Any> handleException(e: Exception): Resource<T> {
        Timber.e(e)
        if (e is HttpException) {
            return handleErrorBody(e.response()?.errorBody()).apply {
                statusCode = e.code()
            }
        }

        return Resource.Error()
    }


    fun handleErrorBody(body: ResponseBody?): Resource.Error {

        return Resource.Error()
    }
}