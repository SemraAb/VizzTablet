package com.samraa.data.models.dto

import com.samraa.data.enums.OrderStatus

data class OrderDto(
    val id: String ,
    val customerName: String,
    val orderDate: String,
    val orderStatus: OrderStatus,
    val isStatusLocked: Boolean = false
)

