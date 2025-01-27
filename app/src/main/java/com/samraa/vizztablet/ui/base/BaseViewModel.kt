package com.samraa.vizztablet.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samraa.data.enums.UiState
import com.samraa.data.models.dto.ErrorDialogDto
import com.samraa.data.utils.Resource
import com.samraa.vizztablet.utils.extension.asErrorDialogModel
import com.samraa.vizztablet.utils.extension.asUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel(){
    private val _showErrorDialog = MutableStateFlow<ErrorDialogDto?>(null)
    val showErrorDialog = _showErrorDialog.asStateFlow()

    private val _showProgressDialog = MutableStateFlow<UiState?>(null)
    val showProgressDialog = _showProgressDialog.asStateFlow()

    protected val _uiState = MutableStateFlow(UiState.LOADING)
    open val uiState = _uiState.asStateFlow()

    fun <T> executeInBackground(
        uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.LOADING),
        checkEmptyList: Boolean = false,
        hasNextRequest: Boolean = false,
        checkErrorState: Boolean = true,
        showErrorDialog: Boolean = true,
        showProgressDialog: Boolean = false,
        func: suspend () -> Resource<T>
    ): Job {
        if (uiState.value != UiState.LOADING)
            uiState.value = UiState.LOADING

        if (showProgressDialog)
            _showProgressDialog.value = uiState.value

        return viewModelScope.launch {
            val response = func()
            val newState = response.asUiState(checkEmptyList)

            if (showErrorDialog && newState == UiState.ERROR)
                showError(response.asErrorDialogModel())

            if (hasNextRequest && newState == UiState.SUCCESS)
                return@launch

            if (checkErrorState || newState != UiState.ERROR) {
//                delay(200)

                uiState.value = newState
            }

            if (showProgressDialog)
                _showProgressDialog.value = uiState.value
        }
    }

    fun showError(model: ErrorDialogDto?) {
        _showErrorDialog.value = model
    }

    fun changeUiState(uiState: UiState) {
        viewModelScope.launch {
            _uiState.emit(uiState)
        }
    }
}