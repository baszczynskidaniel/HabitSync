package org.example.project.habit.presentation.add_habit

import org.example.project.habit.domain.Category

sealed interface AddEditHabitAction {
    data class OnNameChange(val nameChange: String): AddEditHabitAction
    data object OnNameClear: AddEditHabitAction
    data object OnDescriptionClear: AddEditHabitAction
    data object OnUnitClear: AddEditHabitAction
    data class OnDescriptionChange(val descriptionChange: String): AddEditHabitAction
    data class OnUnitChange(val unitChange: String): AddEditHabitAction
    data object OnAddEditHabit: AddEditHabitAction
    data object OnPositiveChange: AddEditHabitAction
    data object OnMeasurableChange: AddEditHabitAction
    data class OnTargetChange(val targetChange: String): AddEditHabitAction
    data class OnCategorySelection(val category: Category, val currentValue: Boolean): AddEditHabitAction


}