package com.samraa.data.models.remote

import com.google.gson.annotations.SerializedName
import com.samraa.data.models.response.BaseResponse


data class OrdersModelItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("orderDate")
    val orderDate: String,
    @SerializedName("customerName")
    val customerName: String,
    @SerializedName("status")
    val status: String
) : BaseResponse()