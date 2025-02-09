package org.example.project.habit.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryHabitDto (
    @SerialName("categoryId") val categoryId: String,
    @SerialName("habitId") val habitId: String,
)


