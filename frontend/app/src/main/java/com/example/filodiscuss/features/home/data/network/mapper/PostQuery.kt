package com.example.filodiscuss.features.home.data.network.mapper

import com.example.filodiscuss.PostQuery
import com.example.filodiscuss.features.auth.domain.model.User
import com.example.filodiscuss.features.home.domain.model.Post

fun PostQuery.Post.toDomain() : Post{
    return Post(
        id = id,
        createdAt = createdAt,
        updatedAt = updatedAt,
        title = title,
        content = text,
        creator = creator.let { creator ->
            User(
                id = creator.id,
                username = creator.username
            )
        },
        points = points,
        voteStatus = voteStatus
    )
}