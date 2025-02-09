package org.example.project.habit.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto(
    @SerialName("userId") val userId: String,
    @SerialName("categoryId") val categoryId: String?,
    @SerialName("name") val name: String,
)

