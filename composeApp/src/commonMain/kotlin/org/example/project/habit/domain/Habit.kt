package org.example.project.habit.domain

import kotlinx.serialization.SerialName
import org.example.project.habit.presentation.habit_list.HabitCompletionState

data class Habit(
    val id: String? = null,
    val name: String = "",
    val description: String? = null,
    val unit: String? = null,
    val isPositive: Boolean = true,
    val isMeasurable: Boolean = true,
    val progressPercentage: Double = 0.0,
    val target: Double = 0.0,
    val hasTarget: Boolean = false,
    val count: Double = 0.0,
   // val atMostTarget: Boolean = false,
   // val question: String? = null,
   // val categories: List<String>? = null
) {
    fun getHabitCompletionState(): HabitCompletionState {
        if(count / target >= 1.0 &&  isPositive) return HabitCompletionState.DONE_POSITIVE
        if(count / target >= 1.0 ) return HabitCompletionState.DONE_NEGATIVE

        return HabitCompletionState.UNDONE

    }


}
