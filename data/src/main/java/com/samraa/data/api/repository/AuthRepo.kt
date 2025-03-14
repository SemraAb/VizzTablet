package com.samraa.data.api.repository

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.samraa.data.api.services.AuthApiService
import com.samraa.data.models.request.LoginRequest
import com.samraa.data.models.request.RegisterRequest
import com.samraa.data.utils.ResponseHandler
import com.samraa.data.utils.createEmptyFile
import com.samraa.data.utils.toFile
import com.samraa.data.utils.toMultipartBodyPart
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class AuthRepo(private val api: AuthApiService, private val context: Context) : BaseRepo() {

    suspend fun login(loginRequest: LoginRequest) = callApi {

        val response = api.login(loginRequest = loginRequest)
        ResponseHandler.handleSuccess(response)
    }

    suspend fun register(registerRequest: RegisterRequest, logoUri: Uri) = callApi {
        // Convert `registerRequest` to JSON
        val gson = Gson()
        val requestJson = gson.toJson(registerRequest)

        // Create `RequestBody` for the `request` key
        val requestBody = requestJson.toRequestBody("application/json".toMediaTypeOrNull())

        val logoPart: MultipartBody.Part

        // Check if logoUri is empty
        if (logoUri == Uri.EMPTY) {
            val emptyFile = createEmptyFile(context)
            logoPart = emptyFile.toMultipartBodyPart("logo")
        } else {
            val logoFile = logoUri.toFile(context)
            logoPart = logoFile.toMultipartBodyPart("logo")
        }

        // Call the API
        val response = api.register(
            request = requestBody,
            logo = logoPart
        )
        ResponseHandler.handleSuccess(response)
    }

}