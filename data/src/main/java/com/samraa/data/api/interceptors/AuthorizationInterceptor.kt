package com.samraa.data.api.interceptors

import com.samraa.data.api.repository.AuthRepo
import com.samraa.data.persistence.SessionManager
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.koin.core.component.KoinComponent
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class AuthorizationInterceptor : Interceptor, KoinComponent {

    override fun intercept(chain: Interceptor.Chain): Response {
        return RefreshIntercept.block( chain)
    }
}

private object RefreshIntercept {
    private val chainList = mutableListOf<Interceptor.Chain>()
    private val responseList = mutableListOf<Response>()
    private val continuationList = mutableListOf<Continuation<Response>>()
    private var apiRequested = false

    fun block(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request().newRequest())
        return runBlocking {
            suspendCoroutine { continuation ->
                when (response.code) {
                    406 -> {

                    }

                    401 -> {

                    }

                    else -> {
                        continuation.resume(response)
                    }
                }
            }
        }
    }

    private fun Request.newRequest() = newBuilder()
        .addHeader("Content-Type", "application/json")
        .build()
}