package com.samraa.data.api.repository

import com.samraa.data.utils.Resource
import com.samraa.data.utils.ResponseHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BaseRepo(private val dispatcher: CoroutineDispatcher = Dispatchers.IO) {

    suspend fun <T> callApi(
        func: suspend () -> Resource<T>
    ): Resource<T> {
        return withContext(dispatcher) {
            try {
                func.invoke()
            } catch (e: Exception) {
                ResponseHandler.handleException(e)
            }
        }
    }
}