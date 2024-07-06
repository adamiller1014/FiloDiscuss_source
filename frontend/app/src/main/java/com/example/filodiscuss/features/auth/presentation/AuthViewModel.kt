package com.example.filodiscuss.features.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filodiscuss.features.auth.domain.repository.AuthRepository
import com.example.filodiscuss.features.auth.presentation.state.LoginState
import com.example.filodiscuss.features.auth.presentation.state.RegisterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun register(username: String, password: String) {
        _registerState.value = RegisterState.Loading
        viewModelScope.launch {
            authRepository.register(username, password).collect { result ->
                result.onSuccess { user ->
                    if (user != null) {
                        _registerState.value = RegisterState.Success(user)
                    } else {
                        _registerState.value = RegisterState.Error("Registration failed.")
                    }
                }.onFailure { exception ->
                    _registerState.value = RegisterState.Error(exception.message ?: "Unknown error")
                }
            }
        }
    }

    fun login(username: String, password: String) {
        _loginState.value = LoginState.Loading
        viewModelScope.launch {
            authRepository.login(username, password).collect { result ->
                result.onSuccess { user ->
                    if (user != null) {
                        _loginState.value = LoginState.Success(user)
                    } else {
                        _loginState.value = LoginState.Error("Login failed.")
                    }
                }.onFailure { exception ->
                    _loginState.value = LoginState.Error(exception.message ?: "Unknown error")
                }
            }
        }
    }
}