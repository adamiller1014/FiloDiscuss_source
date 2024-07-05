package com.example.filodiscuss.features.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filodiscuss.features.auth.domain.model.User
import com.example.filodiscuss.features.auth.domain.repository.AuthRepository
import com.example.filodiscuss.features.auth.domain.use_case.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

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
}

sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    data class Success(val user: User) : RegisterState()
    data class Error(val message: String) : RegisterState()
}