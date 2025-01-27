package com.samraa.vizztablet.ui.auth.register

import android.net.Uri
import android.util.Log
import androidx.core.net.toFile
import com.samraa.data.api.repository.AuthRepo
import com.samraa.data.models.request.RegisterRequest
import com.samraa.data.persistence.SessionManager
import com.samraa.data.utils.onError
import com.samraa.data.utils.onSuccess
import com.samraa.vizztablet.ui.base.BaseViewModel
import com.samraa.vizztablet.utils.CombinedStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class RegisterVM(private val repo: AuthRepo) : BaseViewModel() {

    val email = MutableStateFlow<String>("")
    val password = MutableStateFlow<String>("")
    val passwordReEnter = MutableStateFlow<String>("")
    val location = MutableStateFlow<String>("")
    val logoImage = MutableStateFlow<Uri>(Uri.EMPTY)

    val isEmailValid = CombinedStateFlow(email) {
        android.util.Patterns.EMAIL_ADDRESS.matcher(email.value).matches()
    }

    val isPasswordSame = CombinedStateFlow(password, passwordReEnter) {
        passwordReEnter.value.trim() == password.value.trim() && password.value.trim().isNotEmpty()
    }
    val enableSignUpButton = CombinedStateFlow(email, password, location, passwordReEnter) {
        email.value.isNotEmpty() &&
                password.value.isNotEmpty() &&
                passwordReEnter.value.isNotEmpty() &&
                passwordReEnter.value.trim() == password.value.trim() &&
                location.value.isNotEmpty() &&
                android.util.Patterns.EMAIL_ADDRESS.matcher(email.value).matches()
    }

    val isVisibleDone = MutableStateFlow(false)

    private var _navigateToHome = MutableSharedFlow<Boolean>()
    val navigateToHome = _navigateToHome.asSharedFlow()

    fun register() = executeInBackground {
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
            SessionManager.token = it.accessToken
            SessionManager.loggedIn = true

            _navigateToHome.emit(true)
        }.onError { message, status, _ ->
            Log.e("error", "register:  ${status.name}")
        }
    }
}