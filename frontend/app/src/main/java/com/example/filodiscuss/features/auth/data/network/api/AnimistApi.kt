package com.example.filodiscuss.features.auth.data.network.api

import com.apollographql.apollo3.ApolloClient
import com.example.filodiscuss.LoginMutation
import com.example.filodiscuss.LogoutMutation
import com.example.filodiscuss.MeQuery
import com.example.filodiscuss.RegisterMutation
import com.example.filodiscuss.features.auth.domain.model.User
import com.example.filodiscuss.features.auth.data.network.mapper.toDomain
import com.example.filodiscuss.type.UsernamePasswordInput
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AnimistApi @Inject constructor(
    private val apolloClient: ApolloClient
) {
    fun register(email: String, username: String, password: String): Flow<Result<User?>> {
        return flow {
            val response = apolloClient.mutation(RegisterMutation(UsernamePasswordInput(email= email,username = username, password = password))).execute()
            val errors = response.data?.register?.regularUserResponse?.errors
            val user = response.data?.register?.regularUserResponse?.user

            if (response.hasErrors() || errors != null) {
                val errorMessage = errors?.joinToString { it.regularError.message } ?: response.errors?.firstOrNull()?.message
                emit(Result.failure(Exception(errorMessage ?: "Unknown error")))
            } else {
                emit(Result.success(user?.toDomain()))
            }
        }
    }

    fun login(usernameOrEmail: String, password: String): Flow<Result<User?>> {
        return flow {
            val response = apolloClient.mutation(LoginMutation(usernameOrEmail, password)).execute()
            val errors = response.data?.login?.regularUserResponse?.errors
            val user = response.data?.login?.regularUserResponse?.user

            if (response.hasErrors() || errors != null) {
                val errorMessage = errors?.joinToString { it.regularError.message } ?: response.errors?.firstOrNull()?.message
                emit(Result.failure(Exception(errorMessage ?: "Unknown error")))
            } else {
                emit(Result.success(user?.toDomain()))
            }
        }
    }

    fun getCurrentUser(): Flow<Result<User?>> {
        return flow {
            val response = apolloClient.query(MeQuery()).execute()
            if (response.hasErrors()) {
                emit(Result.failure(Exception(response.errors?.firstOrNull()?.message)))
            } else {
                val user = response.data?.me?.toDomain()
                emit(Result.success(user))
            }
        }
    }

    fun logout(): Flow<Result<Boolean?>> {
        return flow {
            val response = apolloClient.mutation(LogoutMutation()).execute()
            if (response.hasErrors()) {
                emit(Result.failure(Exception(response.errors?.firstOrNull()?.message)))

            } else {
                emit(Result.success(response.data?.logout))
            }
        }
    }
}
