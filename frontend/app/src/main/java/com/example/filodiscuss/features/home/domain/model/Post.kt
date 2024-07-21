package com.example.filodiscuss.features.home.domain.model

import com.example.filodiscuss.features.auth.domain.model.User

data class Post(
    val id: Number,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val title: String,
    val content: String,
    val creator: User? = null,
    val points: Number ? = null,
    val voteStatus : Number? = null
)
