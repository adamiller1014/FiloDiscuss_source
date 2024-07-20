package com.example.filodiscuss.features.home.domain.model

import com.example.filodiscuss.features.auth.domain.model.User

data class Post(
    val id: Number,
    val createdAt: String,
    val updatedAt: String,
    val title: String,
    val content: String,
    val creator: User? = null,
    val points: Number,
    val voteStatus : Number? = null
)
