package com.samraa.vizztablet.ui.auth.register

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.samraa.data.api.repository.AuthRepo
import com.samraa.data.enums.UiState
import com.samraa.data.models.request.RegisterRequest
import com.samraa.data.persistence.SessionManager
import com.samraa.data.utils.onError
import com.samraa.data.utils.onSuccess
import com.samraa.vizztablet.ui.base.BaseViewModel
import com.samraa.vizztablet.utils.CombinedStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class RegisterVM(private val repo: AuthRepo) : BaseViewModel() {

    val email = MutableStateFlow<String>("")
    val password = MutableStateFlow<String>("")
    val passwordReEnter = MutableStateFlow<String>("")
    val location = MutableStateFlow<String>("")
    val logoImage = MutableStateFlow<Uri>(Uri.EMPTY)


    val isPasswordErrHintEnable = CombinedStateFlow(password, passwordReEnter) {
        when {
            password.value.isEmpty() && passwordReEnter.value.isEmpty() -> false
            password.value.isNotEmpty() && passwordReEnter.value.isEmpty() -> false
            password.value.isNotEmpty() && passwordReEnter.value.isNotEmpty() -> password.value.trim() != passwordReEnter.value.trim()
            else -> false
        }
    }

    val enableSignUpButton = CombinedStateFlow(email, password, location, passwordReEnter) {
        email.value.isNotEmpty() &&
                password.value.isNotEmpty() &&
                passwordReEnter.value.isNotEmpty() &&
                passwordReEnter.value.trim() == password.value.trim() &&
                location.value.isNotEmpty()
    }

    val isVisibleDone = MutableStateFlow(false)

    private var _navigateToHome = MutableSharedFlow<Boolean>()
    val navigateToHome = _navigateToHome.asSharedFlow()

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage = _errorMessage.asSharedFlow()

    init {
        viewModelScope.launch {
            _uiState.emit(UiState.SUCCESS)
        }
    }

    fun register() = executeInBackground {
        _uiState.emit(UiState.LOADING)
        repo.register(
            registerRequest = RegisterRequest(
                username = email.value,
                password = password.value,
                location = location.value,
                displayName = "a",
                deviceToken = SessionManager.firebaseToken
            ),
            logoUri = logoImage.value
        ).onSuccess {
            _uiState.emit(UiState.SUCCESS)
            SessionManager.token = it.accessToken
            SessionManager.loggedIn = true

            _navigateToHome.emit(true)
        }.onError { message, status, statusCode ->
            _uiState.emit(UiState.SUCCESS)
            when (statusCode) {
                401 -> _errorMessage.emit("Unauthorized access. Please check your credentials and try again.")
                else -> _errorMessage.emit("Something went wrong!")
            }
        }
    }
}