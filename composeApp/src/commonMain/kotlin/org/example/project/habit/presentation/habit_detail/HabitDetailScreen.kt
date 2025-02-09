package org.example.project.habit.presentation.habit_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import habitsync.composeapp.generated.resources.Res
import habitsync.composeapp.generated.resources.navigate_back
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.example.project.app.Route
import org.example.project.core.domain.onError
import org.example.project.core.domain.onSuccess
import org.example.project.core.presentation.design_system.HSCenterSurface
import org.example.project.core.presentation.ui.AppIcons
import org.example.project.core.presentation.ui.LocalDimensions
import org.example.project.habit.data.dto.UpdateHabitScoreDto
import org.example.project.habit.data.mappers.toHabit
import org.example.project.habit.domain.Category
import org.example.project.habit.domain.Habit
import org.example.project.habit.domain.HabitRepository
import org.example.project.habit.presentation.CircularProgressWithPercentage
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun HabitDetailScreen(
    state: HabitDetailState,
    onAction: (HabitDetailAction) -> Unit,
    onBack: () -> Unit,
    onEdit: (String) -> Unit,
    modifier: Modifier = Modifier,


) {
    HSCenterSurface(
        title = {
            Text(state.habit.name)
        },
        navigationIcon = {
            FilledTonalIconButton(
                onClick = { onBack() }
            ) {
                Icon(AppIcons.BACK, stringResource(Res.string.navigate_back))
            }
        },
        actions = {
            FilledTonalIconButton(
                onClick = {
                    onEdit(state.habit.id!!)
                }
            ) {
                Icon(AppIcons.EDIT, "edit habit")
            }
            FilledTonalIconButton(
                onClick = {
                    onAction(HabitDetailAction.OnRemove)
                    onBack()
                }
            ) {
                Icon(Icons.Default.Delete, null)
            }
        }
    ) {
        HSCard(
            modifier = Modifier
                .widthIn(max = LocalDimensions.current.maxButtonWidth)
                .fillMaxWidth(),
        ) {
            Text(state.habit.name, style = MaterialTheme.typography.labelLarge)
            if(!state.habit.description.isNullOrBlank()) {
                Text(state.habit.description)
            }
        }

        HSCard(
            modifier = Modifier
                .widthIn(max = LocalDimensions.current.maxButtonWidth)
                .fillMaxWidth(),
        ) {
            Text("Today progress", style = MaterialTheme.typography.labelLarge )
            CircularProgressWithPercentage(
                percentage = state.habit.count.toFloat() / state.habit.target.toFloat() * 100f,

            )
        }

        if(state.categories.isNotEmpty()) {
            HSCard(
                modifier = Modifier
                    .widthIn(max = LocalDimensions.current.maxButtonWidth)
                    .fillMaxWidth(),
            ) {
                FlowRow(
                    verticalArrangement = Arrangement.spacedBy(LocalDimensions.current.smallPadding),
                    horizontalArrangement = Arrangement.spacedBy(LocalDimensions.current.smallPadding)
                ) {
                    state.categories.forEach {
                        AssistChip(

                            onClick = {

                            },
                            label = {
                                Text(it.name)
                            },

                            )
                    }

                }

            }
        }

        if(state.habit.isMeasurable) {
            HSCard(
                modifier = Modifier
                    .widthIn(max = LocalDimensions.current.maxButtonWidth)
                    .fillMaxWidth(),
            ) {


            Text("Today count", style = MaterialTheme.typography.labelLarge )
            Text(text = state.habit.count.toString(), style = MaterialTheme.typography.displayMedium.copy(
                color = MaterialTheme.colorScheme.primary
            ))
            Row(
                modifier = modifier
                    .widthIn(LocalDimensions.current.maxButtonWidth)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(LocalDimensions.current.smallPadding)
            ) {
                FilledTonalIconButton(
                    onClick = {
                        onAction(HabitDetailAction.OnHabitCountChange(1.0))
                    }
                ) {
                    Icon(AppIcons.ADD, null)
                }
                FilledTonalIconButton(
                    enabled = state.habit.count >= 0,
                    onClick = {
                        onAction(HabitDetailAction.OnHabitCountChange(-1.0))
                    }
                ) {
                    Text("-")
                }
            }
            }
        } else {
            HSCard(
                modifier = Modifier
                    .widthIn(max = LocalDimensions.current.maxButtonWidth)
                    .fillMaxWidth(),
            ) {
                if(state.habit.count > 0) {
                    IconButton(
                        {onAction(HabitDetailAction.OnHabitCountChange(-1.0))},
                        modifier = Modifier.border(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primary,
                            width = 2.dp,
                        )
                    ) {
                        Icon(
                            imageVector = AppIcons.DONE,
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = null,
                        )
                    }
                } else {
                    IconButton(
                        onClick = {onAction(HabitDetailAction.OnHabitCountChange(1.0))},
                        modifier = Modifier.border(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.onSurface.copy(0.5f),
                            width = 2.dp,
                        )
                    ) {

                    }
                }

            }
        }
    }
}

@Composable
fun HSCard(
    modifier: Modifier = Modifier,
    colors: CardColors = CardDefaults.elevatedCardColors(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurface
    ),
    onClick: () -> Unit = {},
    content: @Composable() (ColumnScope.() -> Unit),


) {
    ElevatedCard(
        modifier = modifier,
        colors = colors,
        shape = RoundedCornerShape(LocalDimensions.current.mediumPadding)
    ) {
        Column(
            modifier = Modifier
                .clickable { onClick() }
                .padding(LocalDimensions.current.mediumPadding)
                .fillMaxWidth()
            ,
            verticalArrangement = Arrangement.spacedBy(LocalDimensions.current.mediumPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            content()
        }

    }
}

data class HabitDetailState(
    val habit: Habit = Habit(),
    val categories: List<Category> = emptyList()
)

sealed class HabitDetailAction {
    data class OnHabitCountChange(val valueChange: Double): HabitDetailAction()
    data class OnSelectedHabitChange(val habit: Habit): HabitDetailAction()
    data object OnRemove: HabitDetailAction()
}

class HabitDetailViewModel(
    private val habitRepository: HabitRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val habitId = savedStateHandle.toRoute<Route.HabitDetail>().habitId

    private val _state = MutableStateFlow(HabitDetailState())
    val state = _state
        .onStart {
            fetchHabit()
            fetchCategories()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    private fun fetchCategories() {
        viewModelScope.launch {
            habitRepository.getHabitCategories(habitId)
                .onSuccess { categories ->
                    _state.update {
                        it.copy(
                            categories = categories
                        )
                    }
                }
                .onError {
                    println(it.name)
                }
        }
    }

    private fun fetchHabit() {
        viewModelScope.launch {
            habitRepository
                .getHabitById(habitId)
                .onError {

                }
                .onSuccess {
                    _state.update { it.copy(
                        habit = it.habit
                    ) }
                }
        }
    }

    fun onAction(action: HabitDetailAction) {
        when(action) {
            is HabitDetailAction.OnHabitCountChange -> {
                viewModelScope.launch {
                    val localDate: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

                    val dto = UpdateHabitScoreDto(
                        habitId = habitId,
                        action.valueChange,
                        date = localDate
                    )
                    habitRepository.updateHabitScore(dto)
                        .onSuccess {

                        }
                    habitRepository
                        .getHabitById(habitId)
                        .onError {

                        }
                        .onSuccess { habit ->
                            _state.update { it.copy(
                                habit = habit.toHabit()
                            ) }
                        }
                    println(_state.value.habit)
                }
            }

            is HabitDetailAction.OnSelectedHabitChange -> {
                _state.update { it.copy(
                    habit = action.habit
                ) }
            }

            HabitDetailAction.OnRemove -> {
                viewModelScope.launch {
                    habitRepository.removeHabitById(habitId)
                }
            }
        }

    }
}