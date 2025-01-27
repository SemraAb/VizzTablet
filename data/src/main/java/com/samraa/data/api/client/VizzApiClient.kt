package com.samraa.data.api.client

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.samraa.data.api.services.AuthApiService
import com.samraa.data.api.services.HomeApiService
import com.samraa.data.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object VizzApiClient {

    fun provideAuthApi(): AuthApiService {
        return getRetrofit().create(
            AuthApiService::class.java
        )
    }

    fun provideHomeApi(): HomeApiService{
        return getRetrofit().create(
            HomeApiService::class.java
        )
    }

    fun getRetrofit(): Retrofit {

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Set the desired logging level
        }


        val client =
            OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
//                .addInterceptor(AuthorizationInterceptor())
                .addNetworkInterceptor(loggingInterceptor).build()

        val gson = GsonBuilder().setPrettyPrinting().create()

        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

}