package org.example.project.habit.presentation.add_habit

import com.plcoding.bookpedia.core.presentation.UiText
import org.example.project.habit.domain.Category
import org.example.project.habit.domain.Habit

data class AddEditHabitState(
    val name: String = "",
    val description: String = "",
    val unit: String = "",
    val isPositive: Boolean = true,
    val isMeasurable: Boolean = false,
    val nameErrorMessage: UiText? = null,
    val target: String = "",
    val categories: List<Category> = emptyList(),
    val selectedCategories: Set<Category> = emptySet()
) {
    fun toHabit() = Habit(
        id = null,
        name = name,
        description = if (description.isBlank()) null else description,
        unit = if (unit.isBlank()) null else unit,
        isPositive = isPositive,
        isMeasurable = isMeasurable,
        hasTarget = isMeasurable,
        target = if(target.isEmpty()) 1.0 else target.toDoubleOrNull()!!

    )
}