package com.samraa.vizztablet.utils.extension

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.samraa.data.enums.UiState
import com.samraa.data.models.dto.ErrorDialogDto
import com.samraa.data.utils.Resource
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

fun <T> StateFlow<T>.observe(
    fragment: Fragment,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    result: (T) -> Unit
) {
    fragment.run {
        lifecycleScope.launch {
            repeatOnLifecycle(state) {
                collect { result(it) }
            }
        }
    }
}

fun <T> Resource<T>.asUiState(checkEmptyList: Boolean = false): UiState {
    return when (this) {
        is Resource.Success -> if (checkEmptyList && this.data is List<*> && (this.data as List<*>).isEmpty()) UiState.EMPTY else UiState.SUCCESS
        is Resource.Error -> UiState.ERROR
    }
}

fun <T> Resource<T>.asErrorDialogModel(): ErrorDialogDto? {
    return when (this) {
        is Resource.Error -> ErrorDialogDto(message = this.message, errorStatus = statusEnum)
        else -> null
    }
}

fun NavController.navigateSafely(@IdRes actionId: Int) {
    currentDestination?.getAction(actionId)?.let { navigate(actionId) }
}

fun NavController.navigateSafely(directions: NavDirections) {
    currentDestination?.getAction(directions.actionId)?.let { navigate(directions) }
}