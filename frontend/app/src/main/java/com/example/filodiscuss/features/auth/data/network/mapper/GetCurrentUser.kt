package com.example.filodiscuss.features.auth.data.network.mapper

import com.example.filodiscuss.MeQuery
import com.example.filodiscuss.features.auth.domain.model.User

fun MeQuery.Me.toDomain(): User {
    return User(
        id = this.id,
        username = this.username
    )
}