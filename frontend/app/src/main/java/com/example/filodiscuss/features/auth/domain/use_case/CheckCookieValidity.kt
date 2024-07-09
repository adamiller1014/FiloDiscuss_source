package com.example.filodiscuss.features.auth.domain.use_case

import com.example.filodiscuss.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CheckCookieValidity @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): Flow<Result<Result<Boolean?>>> {
        return repository
            .checkCookieValidity()
            .map { Result.success(it) }
            .catch { emit(Result.failure(it)) }
    }
}