package com.samraa.data.mapper

import com.samraa.data.enums.OrderStatus
import com.samraa.data.models.dto.OrderDto
import com.samraa.data.models.remote.OrdersModelItem

fun OrdersModelItem.toDto() = OrderDto(
    id = id.toString(),
    orderDate = orderDate,
    orderStatus = OrderStatus.find(status),
    customerName = customerName

)