package com.samraa.vizztablet.ui.auth.login

import android.util.Log
import com.samraa.data.api.repository.AuthRepo
import com.samraa.data.api.repository.HomeRepo
import com.samraa.data.models.request.LoginRequest
import com.samraa.data.persistence.SessionManager
import com.samraa.data.utils.onError
import com.samraa.data.utils.onSuccess
import com.samraa.vizztablet.ui.base.BaseViewModel
import com.samraa.vizztablet.utils.CombinedStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginVM(
    private val repo: AuthRepo,
) : BaseViewModel() {

    val email = MutableStateFlow<String>("")
    val password = MutableStateFlow<String>("")

    val isEmailValid = CombinedStateFlow(email) {
        android.util.Patterns.EMAIL_ADDRESS.matcher(email.value).matches() && email.value.trim().isNotEmpty()
    }

    var enableSignInButton = CombinedStateFlow(email, password) {
        email.value.isNotEmpty() && password.value.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(
            email.value
        ).matches()
    }

    private var _navigateToHome = MutableSharedFlow<Boolean>()
    val navigateToHome = _navigateToHome.asSharedFlow()

    fun signIn() = executeInBackground {
        repo.login(
            loginRequest = LoginRequest(
                username = email.value,
                password = password.value,
                deviceToken = SessionManager.firebaseToken
            )
        ).onSuccess {

            SessionManager.token = it.accessToken
            SessionManager.loggedIn = true

            _navigateToHome.emit(true)
            Log.d("SignInTest success", SessionManager.firebaseToken)

        }.onError { message, status, _ ->
            Log.d("SignInTest error", status.name)
        }
    }

}