package com.example.filodiscuss.features.auth.domain.use_case

import com.example.filodiscuss.features.auth.domain.model.User
import com.example.filodiscuss.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCurrentUser @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): Flow<Result<Result<User?>>> {
        return repository
            .getCurrentUser()
            .map { Result.success(it) }
            .catch { emit(Result.failure(it)) }
    }
}