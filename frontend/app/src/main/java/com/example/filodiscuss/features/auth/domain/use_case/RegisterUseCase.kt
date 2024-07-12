package com.example.filodiscuss.features.auth.domain.use_case

import com.example.filodiscuss.features.auth.domain.model.User
import com.example.filodiscuss.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, username: String, password: String): Flow<Result<Result<User?>>> {
        return repository
            .register(email,username, password)
            .map { Result.success(it) }
            .catch { emit(Result.failure(it)) }
    }
}
