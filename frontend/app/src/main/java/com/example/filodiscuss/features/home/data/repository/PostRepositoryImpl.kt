package com.example.filodiscuss.features.home.data.repository

import com.example.filodiscuss.features.home.data.network.api.AnimistApi
import com.example.filodiscuss.features.home.domain.model.Post
import com.example.filodiscuss.features.home.domain.model.PostResponse
import com.example.filodiscuss.features.home.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val api: AnimistApi,
) : PostRepository {
    override suspend fun getPosts(cursor: String?, limit: Int): Flow<Result<PostResponse>> {
        return api.getPosts(cursor,limit)
    }
    override suspend fun createPost(title:String, content: String): Flow<Result<Post?>> {
        return api.createPost(title, content)
    }
}