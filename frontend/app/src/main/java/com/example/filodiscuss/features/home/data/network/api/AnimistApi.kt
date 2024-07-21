package com.example.filodiscuss.features.home.data.network.api

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.example.filodiscuss.CreatePostMutation
import com.example.filodiscuss.DeletePostMutation
import com.example.filodiscuss.PostQuery
import com.example.filodiscuss.PostsQuery
import com.example.filodiscuss.VoteMutation
import com.example.filodiscuss.features.home.data.network.mapper.toDomain
import com.example.filodiscuss.features.home.domain.model.Post
import com.example.filodiscuss.features.home.domain.model.PostResponse
import com.example.filodiscuss.type.PostInput
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AnimistApi @Inject constructor(
    private val apolloClient: ApolloClient
) {

    fun getPosts(cursor: String?, limit: Int): Flow<Result<PostResponse>> {
        return flow {
            val response = apolloClient.query(PostsQuery(limit = limit, cursor = Optional.present(cursor))).execute()
            if (response.hasErrors()) {
                emit(Result.failure(Exception(response.errors?.firstOrNull()?.message)))
            } else {
                val posts = response.data?.posts?.posts?.map { it.toDomain() } ?: emptyList()
                val hasMore = response.data?.posts?.hasMore ?: false
                emit(Result.success(PostResponse(posts, hasMore)))
            }
        }
}

    fun createPost(title:String, content: String): Flow<Result<Post?>> {
        return flow {
            val response = apolloClient.mutation(CreatePostMutation(PostInput(title =title, text=content ))).execute()
            if (response.hasErrors()) {
                emit(Result.failure(Exception(response.errors?.firstOrNull()?.message)))
            } else {
                val posts = response.data?.createPost?.toDomain()
                emit(Result.success(posts))
            }
        }
    }

    fun vote(postId: Int, value:Int): Flow<Result<Boolean?>> {
        return flow {
            val response = apolloClient.mutation(VoteMutation(postId= postId, value= value)).execute()
            if (response.hasErrors()) {
                emit(Result.failure(Exception(response.errors?.firstOrNull()?.message)))
            } else {
                emit(Result.success(response.data?.vote))
            }
        }
    }

    fun getPost(postId: Int): Flow<Result<Post?>> {
        return flow {
            val response = apolloClient.query(PostQuery(postId=postId)).execute()
            if (response.hasErrors()) {
                emit(Result.failure(Exception(response.errors?.firstOrNull()?.message)))
            } else {
                val post = response.data?.post?.toDomain()
                emit(Result.success(post))
            }
        }
    }

    fun deletePost(postId: Int): Flow<Result<Boolean?>> {
        return flow {
            val response = apolloClient.mutation(DeletePostMutation(deletePostId=postId)).execute()
            if (response.hasErrors()) {
                emit(Result.failure(Exception(response.errors?.firstOrNull()?.message)))
            } else {
                emit(Result.success(response.data?.deletePost))
            }
        }
    }
}