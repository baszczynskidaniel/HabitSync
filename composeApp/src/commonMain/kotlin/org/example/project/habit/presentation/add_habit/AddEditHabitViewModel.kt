package org.example.project.habit.presentation.add_habit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.bookpedia.core.presentation.UiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.core.domain.Result
import org.example.project.core.domain.onError
import org.example.project.core.domain.onSuccess
import org.example.project.habit.data.dto.CategoryHabitDto
import org.example.project.habit.domain.Category
import org.example.project.habit.domain.HabitRepository
import org.example.project.habit.domain.use_cases.ValidateHabitName


class AddEditHabitViewModel(
    private val habitRepository: HabitRepository
): ViewModel() {

    private val validateHabitName: ValidateHabitName = ValidateHabitName()

    private val _state = MutableStateFlow(AddEditHabitState())
    val state = _state
        .onStart {
            fetchCategories()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    suspend fun fetchCategories() {


        habitRepository.getCategories()
            .onSuccess { categories ->
                _state.update { it.copy(
                    categories = categories
                )
                }
            }
    }

    fun onAction(action: AddEditHabitAction) {
        when(action) {
            AddEditHabitAction.OnAddEditHabit -> {
                submit()
            }
            is AddEditHabitAction.OnDescriptionChange -> _state.update { it.copy(
                description = action.descriptionChange
            )
            }
            AddEditHabitAction.OnDescriptionClear -> _state.update { it.copy(
                description = ""
            )
                it.copy()

            }
            AddEditHabitAction.OnMeasurableChange -> _state.update { it.copy(
                isMeasurable = !it.isMeasurable
            )
            }
            is AddEditHabitAction.OnNameChange -> _state.update { it.copy(
                name = action.nameChange,
                nameErrorMessage = null,
            )
            }
            AddEditHabitAction.OnNameClear -> _state.update { it.copy(
                name = "",
                nameErrorMessage = null,
            )
            }
            AddEditHabitAction.OnPositiveChange -> _state.update { it.copy(
                isPositive = !it.isPositive
            )
            }
            is AddEditHabitAction.OnUnitChange -> _state.update { it.copy(
                unit = action.unitChange
            )
            }
            AddEditHabitAction.OnUnitClear -> _state.update { it.copy(
                unit = ""
            )
            }

            is AddEditHabitAction.OnTargetChange -> _state.update { it.copy(
                target = action.targetChange
            ) }

            is AddEditHabitAction.OnCategorySelection -> {
                val selectedCategories = _state.value.selectedCategories.toMutableSet()
                if(!action.currentValue) {
                    selectedCategories.add(action.category)
                } else {
                    selectedCategories.remove(action.category)
                }
                _state.update { it.copy(
                    selectedCategories = selectedCategories
                ) }
            }
        }
    }

    private fun resetState() {
        _state.value = AddEditHabitState().copy(
            categories = _state.value.categories
        )
    }

    private fun submit() {
        val nameResult = validateHabitName.execute(_state.value.name)
        when(nameResult) {
            is Result.Error -> {
                _state.update { it.copy(
                    nameErrorMessage = UiText.DynamicString("name is empty")
                )
                }
            }
            is Result.Success -> {

                viewModelScope.launch {

                    habitRepository.postHabit(
                        _state.value.toHabit()
                    )

                    var newHabitId = ""
                    habitRepository.getHabits()
                        .onSuccess {
                            newHabitId = it.last().id!!
                        }
                    println(newHabitId)
                    println(state.value.selectedCategories)
                    println(state.value)
                    state.value.selectedCategories.forEach {
                        println("category: $it")
                        habitRepository.attachHabitCategory(
                            CategoryHabitDto(
                                habitId = newHabitId,
                                categoryId = it.categoryId!!,
                            )
                        )
                            .onSuccess { println("here") }
                            .onError { println(it.name) }
                    }
                    resetState()
                }

            }
        }
    }
}