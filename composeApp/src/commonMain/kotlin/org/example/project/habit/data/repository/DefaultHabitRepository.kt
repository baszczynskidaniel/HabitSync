package org.example.project.habit.data.repository

import kotlinx.datetime.LocalDate
import org.example.project.core.domain.DataError
import org.example.project.core.domain.EmptyResult
import org.example.project.core.domain.Result
import org.example.project.core.domain.map
import org.example.project.core.domain.onError
import org.example.project.habit.data.dto.CategoryHabitDto
import org.example.project.habit.data.dto.HabitDto
import org.example.project.habit.data.dto.UpdateHabitScoreDto
import org.example.project.habit.data.mappers.toCategory
import org.example.project.habit.data.mappers.toHabit
import org.example.project.habit.data.network.RemoteHabitDataSource
import org.example.project.habit.domain.Category
import org.example.project.habit.domain.Habit
import org.example.project.habit.domain.HabitRepository

class DefaultHabitRepository(
    private val remoteHabitDataSource: RemoteHabitDataSource,
): HabitRepository {
    override suspend fun getHabits(): Result<List<Habit>, DataError.Remote> {
        return remoteHabitDataSource
            .getHabits()
            .map { dto ->
                dto.map { it.toHabit() }

        }
    }

    override suspend fun postHabit(habit: Habit): EmptyResult<DataError.Remote> {
        return remoteHabitDataSource
            .postHabit(habit)
    }

    override suspend fun getHabitById(habitId: String): Result<HabitDto, DataError.Remote> {
        return remoteHabitDataSource
            .getHabitById(habitId)
    }

    override suspend fun updateHabitScore(
        updateHabitScoreDto: UpdateHabitScoreDto
    ): EmptyResult<DataError.Remote> {
        return remoteHabitDataSource.updateHabitScore(updateHabitScoreDto)
    }

    override suspend fun removeHabitById(habitId: String): EmptyResult<DataError.Remote> {
        return remoteHabitDataSource.removeHabitById(habitId)
    }


    override suspend fun getCategories(): Result<List<Category>, DataError.Remote> {
        return remoteHabitDataSource
            .getCategories()
            .map { dto ->
                dto.map { it.toCategory() }
            }
    }

    override suspend fun postCategory(category: Category): EmptyResult<DataError.Remote> {
        return remoteHabitDataSource.postCategory(category)
    }

    override suspend fun removeCategory(categoryId: String): EmptyResult<DataError.Remote> {
        return remoteHabitDataSource.removeCategory(categoryId)
    }

    override suspend fun attachHabitCategory(categoryHabitDto: CategoryHabitDto): EmptyResult<DataError.Remote> {
        return remoteHabitDataSource.attachHabitCategory(categoryHabitDto)
    }

    override suspend fun detachHabitCategory(categoryHabitDto: CategoryHabitDto): EmptyResult<DataError.Remote> {
        return remoteHabitDataSource.detachHabitCategory(categoryHabitDto)
    }

    override suspend fun getHabitCategories(habitId: String): Result<List<Category>, DataError.Remote> {
        return remoteHabitDataSource
            .getHabitCategories(habitId)
            .map { dto ->
                dto.map { it.toCategory() }
            }


    }
}