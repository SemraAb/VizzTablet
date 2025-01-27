package com.samraa.data.api.repository

import com.samraa.data.api.services.HomeApiService
import com.samraa.data.mapper.toDto
import com.samraa.data.models.request.UpdateOrderStatusRequest
import com.samraa.data.utils.ResponseHandler

class HomeRepo(private val api: HomeApiService) : BaseRepo() {

    suspend fun getOrganizationQr(token: String) = callApi {
        val response = api.getOrganizationQr(token)
        ResponseHandler.handleSuccess(response)
    }

    suspend fun getOrganizationLogo(token: String) = callApi {
        val response = api.getOrganizationLogo(token)
        ResponseHandler.handleSuccess(response)
    }
    suspend fun getOrders(token:String) = callApi {
        val response = api.getOrders(token)
        ResponseHandler.handleSuccess(response.map { it.toDto() })
    }

    suspend fun updateOrderStatus(request: UpdateOrderStatusRequest , token: String) = callApi {
        val response = api.updateOrderStatus(token=token, request=request)
        ResponseHandler.handleSuccess(response)
    }
}