package com.example.filodiscuss.features.home.data.network.mapper

import com.example.filodiscuss.PostsQuery
import com.example.filodiscuss.features.auth.domain.model.User
import com.example.filodiscuss.features.home.domain.model.Post

fun PostsQuery.Post.toDomain(): Post {
    return Post(
        id = postSnippet.id,
        createdAt = postSnippet.createdAt,
        updatedAt = postSnippet.updatedAt,
        title = postSnippet.title,
        content = postSnippet.textSnippet,
        creator = postSnippet.creator.let { creator ->
            User(
                id = creator.id,
                username = creator.username
            )
        },
        points = postSnippet.points,
        voteStatus = postSnippet.voteStatus
    )
}
