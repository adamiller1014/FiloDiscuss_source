package com.example.filodiscuss.features.home.data.network.mapper

import com.example.filodiscuss.PostsQuery
import com.example.filodiscuss.features.home.domain.model.Post

fun PostsQuery.Post.toDomain(): Post {
    return Post(
        id = id,
        createdAt = createdAt,
        updatedAt = updatedAt,
        title = title,
        content = textSnippet
    )
}
