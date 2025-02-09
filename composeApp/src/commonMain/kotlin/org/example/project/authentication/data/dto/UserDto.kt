package org.example.project.authentication.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("userId") val userId: String?,
    @SerialName("username") val username: String,
    @SerialName("password") val password: String,
)
