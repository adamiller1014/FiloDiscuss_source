package com.example.filodiscuss.features.home.data.network.mapper

import com.example.filodiscuss.UpdatePostMutation
import com.example.filodiscuss.features.home.domain.model.Post

fun UpdatePostMutation.UpdatePost.toDomain(): Post {
    return Post(
        id = id,
        title = title,
        content = text
    )
}
