package org.example.project.habit.presentation.add_category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.core.domain.onError
import org.example.project.core.domain.onSuccess
import org.example.project.core.presentation.design_system.HSCenterSurface
import org.example.project.core.presentation.design_system.MediumSpacer
import org.example.project.core.presentation.ui.AppIcons
import org.example.project.core.presentation.ui.LocalDimensions
import org.example.project.habit.domain.Category
import org.example.project.habit.domain.HabitRepository
import org.example.project.habit.presentation.add_habit.AddEditHabitAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCategoryScreen(
    state: AddCategoryState,
    onAction: (AddCategoryAction) -> Unit,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    HSCenterSurface(
        title = {
            Text("Add category")
        },
        navigationIcon = {
            FilledTonalIconButton(onBack) {
                Icon(AppIcons.BACK, "navigate back")
            }
        },
        verticalArrangement = Arrangement.Center
    ) {
        MediumSpacer()
        TextField(
            value = state.name,
            onValueChange = { onAction(AddCategoryAction.OnNameChange(it)) },
            label = {
                Text("name")
            },
            placeholder = {
                Text("e.g Sport")
            },
            maxLines = 1,
            modifier = Modifier
                .widthIn(LocalDimensions.current.bigIconButton)
                .fillMaxWidth(),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            )
        )
        MediumSpacer()
        HorizontalDivider(modifier
            .widthIn(LocalDimensions.current.bigIconButton)
            .fillMaxWidth()
        )
        MediumSpacer()
        Button(
            modifier = Modifier
                .widthIn(LocalDimensions.current.bigIconButton)
                .fillMaxWidth(),
            onClick = { onAction(AddCategoryAction.OnAddCategory) }
        ) {
            Text("Add category")
        }

    }
}

data class AddCategoryState(
    val name: String = "",
)

sealed class AddCategoryAction {
    data class OnNameChange(val nameChange: String): AddCategoryAction()
    data object OnAddCategory: AddCategoryAction()
}

class AddCategoryViewModel(
    private val habitRepository: HabitRepository
): ViewModel() {

    private val _state = MutableStateFlow(AddCategoryState())
    val state = _state
        .onStart {  }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    fun onAction(action: AddCategoryAction) {
        when(action) {
            AddCategoryAction.OnAddCategory -> {
                if(_state.value.name.isNotBlank()) {
                    viewModelScope.launch {
                        habitRepository.postCategory(Category(name = _state.value.name))
                            .onSuccess {
                                _state.value = AddCategoryState()
                            }.onError {
                                println(it.name)
                            }
                    }
                }
            }
            is AddCategoryAction.OnNameChange -> _state.update { it.copy(
                name = action.nameChange
            )
            }
        }
    }
}