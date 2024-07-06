package com.example.filodiscuss.features.auth.data.network.api

import com.apollographql.apollo3.ApolloClient
import com.example.filodiscuss.RegisterMutation
import com.example.filodiscuss.features.auth.data.network.mapper.toDomain
import com.example.filodiscuss.features.auth.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AnimistApi @Inject constructor(
    private val apolloClient: ApolloClient
) {
    fun register(username: String, password: String): Flow<Result<User?>> {
        return flow {
            val response = apolloClient.mutation(RegisterMutation(username, password)).execute()
            val errors = response.data?.register?.errors
            val user = response.data?.register?.user

            if (response.hasErrors() || errors != null) {
                val errorMessage = errors?.joinToString { it.message } ?: response.errors?.firstOrNull()?.message
                emit(Result.failure(Exception(errorMessage ?: "Unknown error")))
            } else {
                emit(Result.success(user?.toDomain()))
            }
        }
    }
}
