package com.example.filodiscuss.features.home.domain.repository

import com.example.filodiscuss.features.home.domain.model.Post
import com.example.filodiscuss.features.home.domain.model.PostResponse
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    suspend fun getPosts(cursor: String?, limit: Int): Flow<Result<PostResponse>>
    suspend fun createPost(title: String, content: String): Flow<Result<Post?>>
    suspend fun vote(postId: Int, value: Int): Flow<Result<Boolean?>>
    suspend fun getPost(postId: Int): Flow<Result<Post?>>
    suspend fun deletePost(postId: Int): Flow<Result<Boolean?>>
}