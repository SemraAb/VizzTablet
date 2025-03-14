package com.samraa.data.api.services

import com.samraa.data.models.request.LoginRequest
import com.samraa.data.models.response.LoginResponse
import com.samraa.data.models.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AuthApiService {

    @POST("v1/auth/organization/login")
    suspend fun login(
        @Body loginRequest: LoginRequest,
    ): LoginResponse

    @Multipart
    @POST("v1/auth/organization/register")
    suspend fun register(
        @Part("request") request: RequestBody, // JSON payload as string
        @Part logo: MultipartBody.Part // Image file
    ): RegisterResponse

}