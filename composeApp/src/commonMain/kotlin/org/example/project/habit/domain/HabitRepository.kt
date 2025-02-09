package org.example.project.habit.domain

import kotlinx.datetime.LocalDate
import org.example.project.core.domain.DataError
import org.example.project.core.domain.EmptyResult
import org.example.project.core.domain.Result
import org.example.project.habit.data.dto.CategoryDto
import org.example.project.habit.data.dto.CategoryHabitDto
import org.example.project.habit.data.dto.HabitDto
import org.example.project.habit.data.dto.UpdateHabitScoreDto

interface HabitRepository {
    suspend fun getHabits(): Result<List<Habit>, DataError.Remote>
    suspend fun postHabit(habit: Habit): EmptyResult<DataError.Remote>
    suspend fun getHabitById(habitId: String): Result<HabitDto, DataError.Remote>
    suspend fun updateHabitScore(updateHabitScoreDto: UpdateHabitScoreDto): EmptyResult<DataError.Remote>

    suspend fun removeHabitById(habitId: String): EmptyResult<DataError.Remote>


    suspend fun getCategories(): Result<List<Category>, DataError.Remote>
    suspend fun postCategory(category: Category): EmptyResult<DataError.Remote>
    suspend fun removeCategory(categoryId: String): EmptyResult<DataError.Remote>

    suspend fun attachHabitCategory(categoryHabitDto: CategoryHabitDto): EmptyResult<DataError.Remote>
    suspend fun detachHabitCategory(categoryHabitDto: CategoryHabitDto): EmptyResult<DataError.Remote>

    suspend fun getHabitCategories(habitId: String): Result<List<Category>, DataError.Remote>
}

