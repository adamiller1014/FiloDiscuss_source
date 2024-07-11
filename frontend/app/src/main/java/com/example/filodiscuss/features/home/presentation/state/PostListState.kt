package com.example.filodiscuss.features.home.presentation.state

import com.example.filodiscuss.features.home.domain.model.Post

sealed class PostListState {
    data object Idle : PostListState()
    data object Loading : PostListState()
    data class Success(val posts: List<Post>) : PostListState()
    data class Error(val message: String) : PostListState()
}


