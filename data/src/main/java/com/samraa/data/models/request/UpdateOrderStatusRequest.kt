package com.samraa.data.models.request

import com.google.gson.annotations.SerializedName

data class UpdateOrderStatusRequest(
    @SerializedName("orderId")
    val orderId: Int,
    @SerializedName("status")
    val status: String
)
