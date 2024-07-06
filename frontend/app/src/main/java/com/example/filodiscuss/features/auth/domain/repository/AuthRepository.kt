package com.example.filodiscuss.features.auth.domain.repository

import com.example.filodiscuss.features.auth.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun register(username: String, password: String): Flow<Result<User?>>

    suspend fun login(username: String, password: String): Flow<Result<User?>>
}