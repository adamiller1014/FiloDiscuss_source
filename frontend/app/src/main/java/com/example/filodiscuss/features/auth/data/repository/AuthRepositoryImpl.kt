package com.example.filodiscuss.features.auth.data.repository

import com.example.filodiscuss.features.auth.data.network.api.AnimistApi
import com.example.filodiscuss.features.auth.domain.model.User
import com.example.filodiscuss.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AnimistApi
) : AuthRepository {
    override suspend fun register(username: String, password: String): Flow<Result<User?>> {
        return api.register(username, password)
    }
}