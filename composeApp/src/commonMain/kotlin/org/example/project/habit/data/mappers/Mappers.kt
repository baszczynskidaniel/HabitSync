package org.example.project.habit.data.mappers

import org.example.project.habit.data.dto.CategoryDto
import org.example.project.habit.data.dto.HabitDto
import org.example.project.habit.domain.Category
import org.example.project.habit.domain.Habit

fun HabitDto.toHabit(): Habit {
    return Habit(
        id = habitId,
        name = name,
        description = if(description.isNullOrBlank()) null else description,
        unit = if(unit.isNullOrBlank()) null else unit,
       // atMostTarget = false,
        isPositive = isPositive,
        isMeasurable = isMeasurable,
        progressPercentage = progressPercentage ?: 0.0,
        target = target,
        count = count
    )
}

fun CategoryDto.toCategory(): Category {
    return Category(
        name = name,
        categoryId = categoryId
    )
}