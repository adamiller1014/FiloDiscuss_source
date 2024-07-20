package com.example.filodiscuss.features.home.presentation.state

import com.example.filodiscuss.features.home.domain.model.Post

sealed class PostDetailState {
    data object Idle : PostDetailState()
    data object Loading : PostDetailState()
    data class Success(val post: Post) : PostDetailState()
    data class Error(val message: String) : PostDetailState()
}
