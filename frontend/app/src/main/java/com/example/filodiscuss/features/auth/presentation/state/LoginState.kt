package com.example.filodiscuss.features.auth.presentation.state

import com.example.filodiscuss.features.auth.domain.model.User

sealed class LoginState {
    data object Idle : LoginState()
    data object Loading : LoginState()
    data class Success(val user: User) : LoginState()
    data class Error(val message: String) : LoginState()
}