package org.example.project.habit.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.example.project.habit.domain.Habit

class SelectedHabitViewModel : ViewModel() {

    private val _selectedHabit = MutableStateFlow<Habit?>(null)
    val selectedHabit = _selectedHabit.asStateFlow()

    fun onSelectedHabitChange(habit: Habit?) {
        _selectedHabit.value = habit
    }
}