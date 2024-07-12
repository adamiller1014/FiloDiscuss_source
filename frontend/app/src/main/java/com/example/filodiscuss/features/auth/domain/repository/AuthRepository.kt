package com.example.filodiscuss.features.auth.domain.repository

import com.example.filodiscuss.features.auth.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun register(email: String, username: String, password: String): Flow<Result<User?>>

    suspend fun login(usernameOrEmail: String, password: String): Flow<Result<User?>>

    suspend fun getCurrentUser(): Flow<Result<User?>>

    suspend fun checkCookieValidity(): Flow<Result<Boolean?>>

    suspend fun logout(): Flow<Result<Boolean? /*Unit*/>>
}