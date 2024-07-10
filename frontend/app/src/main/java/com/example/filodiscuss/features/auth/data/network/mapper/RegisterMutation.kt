package com.example.filodiscuss.features.auth.data.network.mapper

import com.example.filodiscuss.RegisterMutation
import com.example.filodiscuss.features.auth.domain.model.User

fun RegisterMutation.User.toDomain(): User {
    return User(
        id = this.regularUser.id,
        username = this.regularUser.username
    )
}