package com.samraa.data.api.services

import com.samraa.data.models.remote.OrdersModelItem
import com.samraa.data.models.request.UpdateOrderStatusRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT

interface HomeApiService {

    @GET("v1/organization/qr")
    suspend fun getOrganizationQr(
        @Header("Authorization") token: String,
    ): Response<ResponseBody>

    @GET("v1/organization/logo")
    suspend fun getOrganizationLogo(
        @Header("Authorization") token: String,
        @Header("accept") accept: String = "image/png"
    ): Response<ResponseBody>

    @GET("v1/order")
    suspend fun getOrders(
        @Header("Authorization") token: String,
    ): List<OrdersModelItem>

    @PUT("v1/order")
    suspend fun updateOrderStatus(
        @Header("Authorization") token: String,
        @Body request: UpdateOrderStatusRequest
    ): Response<Unit>

}