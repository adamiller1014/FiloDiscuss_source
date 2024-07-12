package com.example.filodiscuss.features.auth.domain.use_case

import com.example.filodiscuss.features.auth.domain.model.User
import com.example.filodiscuss.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(usernameOrEmail: String, password: String): Flow<Result<Result<User?>>> {
        return repository
            .login(usernameOrEmail, password)
            .map { Result.success(it) }
            .catch { emit(Result.failure(it)) }
    }
}