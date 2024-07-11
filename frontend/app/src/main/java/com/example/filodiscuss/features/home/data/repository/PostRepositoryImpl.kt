package com.example.filodiscuss.features.home.data.repository

import com.example.filodiscuss.features.home.data.network.api.AnimistApi
import com.example.filodiscuss.features.home.domain.model.Post
import com.example.filodiscuss.features.home.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val api: AnimistApi,
) : PostRepository {
    override suspend fun getPost(): Flow<Result<List<Post>?>> {
        return api.getPosts()
    }
}