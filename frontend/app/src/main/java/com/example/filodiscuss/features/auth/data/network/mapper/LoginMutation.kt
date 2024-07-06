package com.example.filodiscuss.features.auth.data.network.mapper

import com.example.filodiscuss.LoginMutation
import com.example.filodiscuss.features.auth.domain.model.User

fun LoginMutation.User.toDomain(): User {
    return User(
        id = this.id,
        username = this.username
    )
}