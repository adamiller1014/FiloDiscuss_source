package com.example.filodiscuss.features.home.domain.model

data class PostResponse(
    val posts: List<Post>,
    val hasMore: Boolean
)
