package com.example.filodiscuss.features.home.data.network.mapper

import com.example.filodiscuss.CreatePostMutation
import com.example.filodiscuss.features.home.domain.model.Post

fun CreatePostMutation.CreatePost.toDomain(): Post {
    return Post(
        id = this.id,
        title = this.title,
        content = this.text,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}
