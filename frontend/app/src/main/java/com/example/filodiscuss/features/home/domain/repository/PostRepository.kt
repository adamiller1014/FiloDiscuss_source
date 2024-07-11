package com.example.filodiscuss.features.home.domain.repository

import com.example.filodiscuss.features.home.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    suspend fun getPost() : Flow<Result<List<Post>?>>
}