package com.example.filodiscuss.features.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filodiscuss.features.auth.domain.model.User
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


    private val _currentUserState = MutableStateFlow<User?>(null)
    val currentUserState: StateFlow<User?> = _currentUserState

    private val _checkCookieValidity = MutableStateFlow<Boolean?>(null)
    val checkCookieValidity: StateFlow<Boolean?> = _checkCookieValidity


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

    fun getCurrentUser() {
        viewModelScope.launch {
            authRepository.getCurrentUser().collect { result ->
                result.onSuccess { user ->
                    _currentUserState.value = user
                }.onFailure {
                    _currentUserState.value = null
                }
            }
        }
    }

    fun checkCookieValidity() {
        viewModelScope.launch {
            authRepository.checkCookieValidity().collect { result ->
                result.onSuccess { isValid ->
                    _checkCookieValidity.value = isValid
                }.onFailure {
                    _checkCookieValidity.value = false
                }
            }
        }
    }
}