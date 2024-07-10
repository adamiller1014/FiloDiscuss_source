package com.example.filodiscuss.features.auth.domain.use_case

import com.example.filodiscuss.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): Flow<Result<Unit>> {
        return repository.logout()
    }
}