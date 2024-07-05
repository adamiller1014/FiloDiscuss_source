package com.example.filodiscuss.features.auth.data.network.mapper

import com.example.filodiscuss.RegisterMutation
import com.example.filodiscuss.features.auth.domain.model.User

fun RegisterMutation.User.toDomain(): User {
    return User(
        id = this.id,
        username = this.username
        // Add more fields as needed
    )
}