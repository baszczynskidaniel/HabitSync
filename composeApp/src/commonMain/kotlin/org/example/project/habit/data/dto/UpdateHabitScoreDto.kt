package org.example.project.habit.data.dto

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateHabitScoreDto (
    @SerialName("habitId") val habitId: String?,
    @SerialName("scoreChange") val double: Double,
    @SerialName("date") val date: LocalDate,
)