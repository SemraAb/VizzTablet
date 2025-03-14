package com.samraa.vizztablet.ui.auth.login

import androidx.lifecycle.viewModelScope
import com.samraa.data.api.repository.AuthRepo
import com.samraa.data.enums.UiState
import com.samraa.data.models.request.LoginRequest
import com.samraa.data.models.response.LoginResponse
import com.samraa.data.persistence.SessionManager
import com.samraa.data.utils.ResponseStatus
import com.samraa.data.utils.onError
import com.samraa.data.utils.onSuccess
import com.samraa.vizztablet.ui.base.BaseViewModel
import com.samraa.vizztablet.utils.CombinedStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class LoginVM(
    private val repo: AuthRepo,
) : BaseViewModel() {

    val email = MutableStateFlow<String>("")
    val password = MutableStateFlow<String>("")

    val isEmailValid = CombinedStateFlow(email) {
        android.util.Patterns.EMAIL_ADDRESS.matcher(email.value).matches() && email.value.trim()
            .isNotEmpty()
    }

    var enableSignInButton = CombinedStateFlow(email, password) {
        email.value.isNotEmpty() && password.value.isNotEmpty()
    }

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage = _errorMessage.asSharedFlow()


    private var _navigateToHome = MutableSharedFlow<Boolean>()
    val navigateToHome = _navigateToHome.asSharedFlow()

    init {
        viewModelScope.launch {
            _uiState.emit(UiState.SUCCESS)

        }
    }

    fun signIn() = executeInBackground {
        _uiState.emit(UiState.LOADING)
        repo.login(
            loginRequest = LoginRequest(
                username = email.value,
                password = password.value,
                deviceToken = SessionManager.firebaseToken
            )
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