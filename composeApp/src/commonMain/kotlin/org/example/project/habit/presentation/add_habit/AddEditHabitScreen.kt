package org.example.project.habit.presentation.add_habit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import org.example.project.core.presentation.design_system.HSCenterSurface
import org.example.project.core.presentation.design_system.MediumSpacer
import org.example.project.core.presentation.ui.AppIcons
import org.example.project.core.presentation.ui.LocalDimensions
import org.example.project.habit.domain.Category

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditHabitScreen(
    onBack: () -> Unit,
    state: AddEditHabitState,
    onAction: (AddEditHabitAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    HSCenterSurface(
        title = {
            Text("Add habit")
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
            onValueChange = { onAction(AddEditHabitAction.OnNameChange(it)) },
            label = {
                Text("name")
            },
            placeholder = {
                Text("name")
            },
            maxLines = 1,
            modifier = Modifier
                .widthIn(LocalDimensions.current.bigIconButton)
                .fillMaxWidth(),
            trailingIcon = {
                if(state.name.isNotBlank()) {
                    IconButton(
                        { onAction(AddEditHabitAction.OnNameClear) }
                    ) {
                        Icon(
                            AppIcons.CLEAR, "clear name"
                        )
                    }
                }
            },
            isError = state.nameErrorMessage != null,
            supportingText = {
                if(state.nameErrorMessage != null) {
                    Text(state.nameErrorMessage.toString())
                }
            },
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            )
        )
        MediumSpacer()
        TextField(
            value = state.description,
            onValueChange = { onAction(AddEditHabitAction.OnDescriptionChange(it)) },
            label = {
                Text("description")
            },
            placeholder = {
                Text("description")
            },
            maxLines = 1,
            modifier = Modifier
                .widthIn(LocalDimensions.current.bigIconButton)
                .fillMaxWidth(),
            trailingIcon = {
                if(state.description.isNotBlank()) {
                    IconButton(
                        { onAction(AddEditHabitAction.OnDescriptionClear) }
                    ) {
                        Icon(
                            AppIcons.CLEAR, "clear description"
                        )
                    }
                }
            },
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            )

        )
        MediumSpacer()
        ListItem(
            modifier = Modifier
                .widthIn(LocalDimensions.current.bigIconButton)
                .fillMaxWidth()
                .clickable { onAction(AddEditHabitAction.OnPositiveChange) },
            headlineContent = { Text("Is positive") },
            trailingContent = {
                Checkbox(
                    state.isPositive,
                    onCheckedChange = {
                        onAction(AddEditHabitAction.OnPositiveChange)
                    }
                )
            }
        )
        MediumSpacer()
        ListItem(
            modifier = Modifier
                .widthIn(LocalDimensions.current.bigIconButton)
                .fillMaxWidth()
                .clickable { onAction(AddEditHabitAction.OnMeasurableChange) },
            headlineContent = { Text("Is measurable?")},
            supportingContent = { Text("Can habit be counted e.g. how many miles run")},
            trailingContent = {
                Checkbox(
                    state.isMeasurable,
                    onCheckedChange = {
                        onAction(AddEditHabitAction.OnMeasurableChange)
                    }
                )
            }
        )
        MediumSpacer()
        CategoriesList(
            categories = state.categories,
            selected = state.selectedCategories,
            onClick = { category, selected ->
                onAction(AddEditHabitAction.OnCategorySelection(category, selected))
            }
        )

        androidx.compose.animation.AnimatedVisibility(
            state.isMeasurable
        ) {
            Column {
            MediumSpacer()
            TextField(
                value = state.unit,
                onValueChange = { onAction(AddEditHabitAction.OnUnitChange(it)) },
                label = {
                    Text("unit")
                },
                placeholder = {
                    Text("unit e.g. km")
                },
                maxLines = 1,
                modifier = Modifier
                    .widthIn(LocalDimensions.current.bigIconButton)
                    .fillMaxWidth(),
                trailingIcon = {
                    if(state.unit.isNotBlank()) {
                        IconButton(
                            { onAction(AddEditHabitAction.OnUnitClear) }
                        ) {
                            Icon(
                                AppIcons.CLEAR, "clear unit"
                            )
                        }
                    }
                },
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )
            )
            MediumSpacer()
            TextField(
                modifier = Modifier
                    .widthIn(LocalDimensions.current.bigIconButton)
                    .fillMaxWidth(),
                value = state.target,
                onValueChange = { onAction(AddEditHabitAction.OnTargetChange(it)) },
                label = {
                    Text("target")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
                maxLines = 1,
                placeholder = {
                    Text("e.g. 7")
                },
            )
                }
        }
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
            onClick = { onAction(AddEditHabitAction.OnAddEditHabit) }
        ) {
            Text("Add habit")
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategoriesList(
    categories: List<Category>,
    modifier: Modifier = Modifier,
    selected: Set<Category>,
    onClick: (Category, Boolean) -> Unit,
) {
    FlowRow(
        verticalArrangement = Arrangement.spacedBy(LocalDimensions.current.smallPadding),
        horizontalArrangement = Arrangement.spacedBy(LocalDimensions.current.smallPadding)
    ) {
        categories.forEach {
            ElevatedFilterChip(
                selected = it in selected,
                onClick = {
                    onClick(it, it in selected)
                },
                label = {
                    Text(it.name)
                },

            )
        }

    }
}
