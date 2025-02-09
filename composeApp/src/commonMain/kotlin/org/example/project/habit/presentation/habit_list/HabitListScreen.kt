package org.example.project.habit.presentation.habit_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.bookpedia.core.presentation.UiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import org.example.project.core.domain.onError
import org.example.project.core.domain.onSuccess
import org.example.project.core.presentation.design_system.HSCenterSurface
import org.example.project.core.presentation.design_system.HSTopAppBar
import org.example.project.core.presentation.toUiText
import org.example.project.core.presentation.ui.AppIcons
import org.example.project.core.presentation.ui.LocalDimensions
import org.example.project.habit.domain.Habit
import org.example.project.habit.domain.HabitRepository
import org.example.project.habit.presentation.CircularProgressWithPercentage
import org.example.project.habit.presentation.habit_detail.HSCard

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HabitListScreen(
    state: HabitListState,
    onAction: (HabitListAction) -> Unit,
    onAddHabit: () -> Unit,
    onSettings: () -> Unit,
    onHabitDetail: (Habit) -> Unit,
    modifier: Modifier = Modifier,
    onCategories: () -> Unit,

) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(LocalDimensions.current.mediumPadding)
    ) {
        HSTopAppBar(
            title = { Text("Habits") },
            actions = {
                FilledTonalButton(
                    onClick = onCategories,

                    ) {
                    Text("Categories")

                }
                FilledTonalButton(
                    onClick = onAddHabit,

                ) {
                    Text("Add habit")
                    Icon(Icons.Default.Add, "add habit")
                }
                FilledTonalIconButton(
                    onClick = onSettings
                ) {
                    Icon(AppIcons.SETTINGS, null)
                }

            }
        )
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(LocalDimensions.current.mediumPadding),
          //  verticalArrangement = Arrangement.spacedBy(LocalDimensions.current.mediumPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (state.isLoading) {
                CircularProgressIndicator()
            } else {

                FlowRow(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalArrangement = Arrangement.spacedBy(LocalDimensions.current.smallPadding)
                ) {
                    state.habits.forEach {
                        var completionState by remember {
                            mutableStateOf(
                                it.getHabitCompletionState()
                            )
                        }
                        Box(modifier = Modifier.padding(LocalDimensions.current.smallPadding)) {
                            HabitComponent(
                                it,
                                modifier = Modifier
                                    .widthIn(max = LocalDimensions.current.maxButtonWidth)
                                    .fillMaxWidth()
                                    .clickable { onHabitDetail(it) }
                                ,
                                onClick = { onHabitDetail(it) },
                                state = completionState

                            )
                            if (state.errorMessage != null) {
                                Text(state.errorMessage.toString())
                            }
                        }
                    }
                }
            }
        }

    }


}

data class HabitListState(
    val habits: List<Habit> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null,

)

sealed interface HabitListAction {

}

class HabitListViewModel(
    private val habitRepository: HabitRepository
): ViewModel() {
    private var _state = MutableStateFlow(HabitListState())
    val state = _state
        .onStart {
            habitRepository
                .getHabits()
                .onSuccess { habits ->
                    _state.update { it.copy(
                        habits = habits,
                        isLoading = false,
                        errorMessage = null
                    ) }
                }
                .onError { error ->
                    _state.update { it.copy(
                        isLoading = false,
                        errorMessage = error.toUiText()
                    ) }
                }

        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    fun onAction(action: HabitListAction) {

    }

}

@Composable
fun HabitComponent(
    habit: Habit,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    state: HabitCompletionState,
) {
    var containerColor by remember {
        mutableStateOf(
           Color.Transparent
        )
    }

    var contentColor by remember {
        mutableStateOf(
            Color.Transparent
        )
    }

    var headlineColor by remember {
        mutableStateOf(
            Color.Transparent
        )
    }

    headlineColor = when(state) {
        HabitCompletionState.DONE_POSITIVE -> MaterialTheme.colorScheme.primary
        HabitCompletionState.DONE_NEGATIVE -> MaterialTheme.colorScheme.error
        HabitCompletionState.UNDONE -> MaterialTheme.colorScheme.primary
    }


    containerColor = when(state) {
        HabitCompletionState.DONE_POSITIVE -> MaterialTheme.colorScheme.primaryContainer
        HabitCompletionState.DONE_NEGATIVE -> MaterialTheme.colorScheme.errorContainer
        HabitCompletionState.UNDONE -> MaterialTheme.colorScheme.surfaceVariant
    }

    contentColor = when(state) {
        HabitCompletionState.DONE_POSITIVE -> MaterialTheme.colorScheme.onPrimaryContainer
        HabitCompletionState.DONE_NEGATIVE -> MaterialTheme.colorScheme.onErrorContainer
        HabitCompletionState.UNDONE -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    HSCard(
        modifier = Modifier

            .widthIn(max = LocalDimensions.current.maxButtonWidth)
            .fillMaxWidth()
            //.clickable { onClick() }
        ,
        colors = CardDefaults.elevatedCardColors(
            containerColor = containerColor,
            contentColor = contentColor,
        ),
        onClick = onClick

    ) {
        Row(
            modifier = Modifier.fillMaxWidth().clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(habit.name, style = MaterialTheme.typography.titleLarge)
            CircularProgressWithPercentage(
                habit.count.toFloat() / habit.target.toFloat() * 100f,
            )
        }

    }

//    ListItem(
//        colors = ListItemDefaults.colors(
//            containerColor = containerColor,
//            headlineColor = headlineColor,
//            supportingColor = contentColor,
//        ),
//        modifier = modifier
//
//            .clip(RoundedCornerShape(LocalDimensions.current.mediumClip))
//            .padding(LocalDimensions.current.mediumClip)
//
//
//            .clickable { onClick() },
//        headlineContent = {
//            Text(habit.name)
//        },
//        supportingContent = {
//            if(habit.description != null)
//                Text(habit.description)
//        },
//        trailingContent = {
//            CircularProgressWithPercentage(
//                habit.count.toFloat() / habit.target.toFloat() * 100f,
//            )
//        }
//
//    )
}

enum class HabitCompletionState {
    DONE_POSITIVE,
    DONE_NEGATIVE,
    UNDONE
}

