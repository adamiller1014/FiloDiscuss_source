package com.example.filodiscuss.features.home.data.network.mapper

import com.example.filodiscuss.PostsQuery
import com.example.filodiscuss.features.home.domain.model.Post

fun PostsQuery.Data.toDomain(): List<Post> {
    return this.posts.map {
        Post(
            id = it.id,
            createdAt = it.createdAt,
            updatedAt = it.updatedAt,
            title = it.title
        )
    }
}
