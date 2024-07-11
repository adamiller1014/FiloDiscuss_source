package com.example.filodiscuss.features.home.data.network.api

import com.apollographql.apollo3.ApolloClient
import com.example.filodiscuss.PostsQuery
import com.example.filodiscuss.features.home.data.network.mapper.toDomain
import com.example.filodiscuss.features.home.domain.model.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AnimistApi @Inject constructor(
    private val apolloClient: ApolloClient
) {

    fun getPosts(): Flow<Result<List<Post>?>> {
        return flow {
            val response = apolloClient.query(PostsQuery()).execute()
            if (response.hasErrors()) {
                emit(Result.failure(Exception(response.errors?.firstOrNull()?.message)))
            } else {
                val posts = response.data?.toDomain()?: emptyList()
                emit(Result.success(posts))
            }
        }
    }
}