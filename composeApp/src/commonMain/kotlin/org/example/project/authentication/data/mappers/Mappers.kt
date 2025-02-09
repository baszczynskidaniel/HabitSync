package org.example.project.authentication.data.mappers

import org.example.project.authentication.data.dto.UserDto
import org.example.project.authentication.domain.User

fun UserDto.toUser(): User {
    return User(
        username = username,
        password = password,
    )
}