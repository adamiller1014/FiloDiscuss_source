package com.example.filodiscuss.features.home.presentation.state

import com.example.filodiscuss.features.home.domain.model.Post

sealed class CreatePostState {
    data object Idle : CreatePostState()
    data object Loading : CreatePostState()
    data class Success(val post: Post?) : CreatePostState()
    data class Error(val message: String) : CreatePostState()
}