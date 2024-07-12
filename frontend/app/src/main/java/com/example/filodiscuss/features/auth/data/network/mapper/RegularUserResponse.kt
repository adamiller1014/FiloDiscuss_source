package com.example.filodiscuss.features.auth.data.network.mapper

import com.example.filodiscuss.features.auth.domain.model.User
import com.example.filodiscuss.fragment.RegularUserResponse

fun RegularUserResponse.User.toDomain(): User {
    return User(
        id = this.regularUser.id,
        username = this.regularUser.username
    )
}