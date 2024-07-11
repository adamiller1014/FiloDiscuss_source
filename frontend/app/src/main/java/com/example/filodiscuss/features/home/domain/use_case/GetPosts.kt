package com.example.filodiscuss.features.home.domain.use_case

import com.example.filodiscuss.features.home.domain.model.Post
import com.example.filodiscuss.features.home.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetPosts @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(): Flow<Result<Result<List<Post>?>>> {
        return repository
            .getPost()
            .map { Result.success(it) }
            .catch { emit(Result.failure(it)) }
    }
}