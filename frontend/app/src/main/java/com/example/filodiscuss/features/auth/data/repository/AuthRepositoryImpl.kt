package com.example.filodiscuss.features.auth.data.repository

import com.example.filodiscuss.features.auth.data.local.PreferencesLocal
import com.example.filodiscuss.features.auth.data.network.api.AnimistApi
import com.example.filodiscuss.features.auth.data.network.di.UserCookieJar
import com.example.filodiscuss.features.auth.domain.model.User
import com.example.filodiscuss.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.HttpUrl.Companion.toHttpUrl
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AnimistApi,
    private val userCookieJar: UserCookieJar
) : AuthRepository {
    override suspend fun register(username: String, password: String): Flow<Result<User?>> {
        return api.register(username, password)
    }

    override suspend fun login(username: String, password: String): Flow<Result<User?>> {
        return api.login(username, password)
    }

    override suspend fun getCurrentUser(): Flow<Result<User?>> {
        return api.getCurrentUser()
    }

    override suspend fun checkCookieValidity(): Flow<Result<Boolean>> {
        return flow {
            val cookies = userCookieJar.loadForRequest("http://10.0.2.2:4000/graphql".toHttpUrl())
            val isValid = cookies.any { (it.expiresAt > System.currentTimeMillis()) }
            emit(Result.success(isValid))
        }
    }

    override suspend fun logout(): Flow<Result<Unit>> {
        return flow {
            userCookieJar.clear()
            emit(Result.success(Unit))
        }
    }

}