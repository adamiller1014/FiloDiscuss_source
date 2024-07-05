package com.example.filodiscuss.features.auth.data.network.api

import com.apollographql.apollo3.ApolloClient
import com.example.filodiscuss.RegisterMutation
import com.example.filodiscuss.features.auth.data.network.mapper.toDomain
import com.example.filodiscuss.features.auth.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AnimistApi @Inject constructor(
    private val apolloClient: ApolloClient
) {
    fun register(username: String, password: String): Flow<Result<User?>> {
        return flow {
            val response = apolloClient.mutation(RegisterMutation(username, password)).execute()
            if (response.hasErrors()) {
                emit(Result.failure(Exception(response.errors?.firstOrNull()?.message)))
            } else {
                val user = response.data?.register?.user?.toDomain()
                emit(Result.success(user))
            }
        }
    }
}
