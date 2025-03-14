package com.samraa.vizztablet.ui.home

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.samraa.data.api.repository.HomeRepo
import com.samraa.data.enums.OrderStatus
import com.samraa.data.models.dto.OrderDto
import com.samraa.data.models.request.UpdateOrderStatusRequest
import com.samraa.data.persistence.SessionManager
import com.samraa.data.utils.onSuccess
import com.samraa.vizztablet.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeVM(private val repo: HomeRepo) : BaseViewModel() {
    private val _orderList = MutableStateFlow<List<OrderDto>>(emptyList<OrderDto>())
    val orderList = _orderList.asStateFlow()

    private val _filteredOrders = MutableStateFlow<List<OrderDto>>(emptyList())  // Filtered orders
    val filteredOrders = _filteredOrders.asStateFlow()

    private val _toggle = MutableStateFlow<Boolean>(true)
    val toggle = _toggle.asStateFlow()

    private val _organizationQr = MutableStateFlow<Bitmap?>(null)
    val organizationQr = _organizationQr.asStateFlow()

    private val _organizationLogo = MutableStateFlow<Bitmap?>(null)
    val organizationLogo = _organizationLogo.asStateFlow()

    private val _selectedStatus = MutableStateFlow<OrderStatus?>(null)
    val selectedStatus = _selectedStatus.asStateFlow()

    val search = MutableStateFlow("")

    init {
        getOrganizationQr()
        getOrganizationLogo()
        getOrders()
    }

    fun setToggle() {
        _toggle.value = !_toggle.value
    }


    private fun getOrganizationQr() = executeInBackground {
        repo.getOrganizationQr(token = SessionManager.token).onSuccess { response ->
            val inputStream = response.body()?.byteStream()
            val bitmap = BitmapFactory.decodeStream(inputStream)

            _organizationQr.emit(bitmap)
        }
    }


    private fun getOrganizationLogo() = executeInBackground {
        repo.getOrganizationLogo(token = SessionManager.token).onSuccess { responseBody ->
            val inputStream = responseBody.body()?.byteStream()
            val bitmap = BitmapFactory.decodeStream(inputStream)

            _organizationLogo.emit(bitmap)

        }
    }

    fun getOrders() = executeInBackground(uiState = _uiState, checkEmptyList = true) {
        repo.getOrders(token = SessionManager.token).onSuccess {
            val updatedOrders = it.map { order ->
                if (order.orderStatus != OrderStatus.IN_PROGRESS) {
                    order.copy(isStatusLocked = true) // Lock the row if the status is not in progress
                } else {
                    order
                }
            }
            _orderList.emit(updatedOrders)
            filterOrders()
        }
    }

    fun updateOrderStatus(order: OrderDto, orderStatus: OrderStatus) = executeInBackground {
        repo.updateOrderStatus(
            token = SessionManager.token,
            request = UpdateOrderStatusRequest(
                orderId = order.id.toInt(),
                status = orderStatus.name
            )
        ).onSuccess {
            _orderList.value = _orderList.value.map {
                if (it.id == order.id) it.copy(
                    orderStatus = orderStatus,
                    isStatusLocked = true
                ) else it
            }
            filterOrders()
        }

    }


    fun setSelectedStatus(status: OrderStatus?) {
        _selectedStatus.value = status
        filterOrders()
    }


    private fun filterOrders() {
        val currentStatus = _selectedStatus.value
        _filteredOrders.value = if (currentStatus != null) {
            _orderList.value.filter { it.orderStatus == currentStatus }
        } else {
            _orderList.value  // If no status selected, show all orders
        }
    }
}