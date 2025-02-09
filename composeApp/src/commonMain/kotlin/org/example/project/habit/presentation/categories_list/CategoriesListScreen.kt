package org.example.project.habit.presentation.categories_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.app.App
import org.example.project.core.domain.onError
import org.example.project.core.domain.onSuccess
import org.example.project.core.presentation.design_system.HSTopAppBar
import org.example.project.core.presentation.design_system.SmallSpacer
import org.example.project.core.presentation.ui.AppIcons
import org.example.project.core.presentation.ui.LocalDimensions
import org.example.project.habit.domain.Category
import org.example.project.habit.domain.HabitRepository
import org.example.project.habit.presentation.add_category.AddCategoryAction
import org.example.project.habit.presentation.habit_detail.HSCard

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CategoriesListScreen(
    state: CategoriesListState,
    onAction: (CategoriesListAction) -> Unit,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onAddCategory: () -> Unit,
    onSettings: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(LocalDimensions.current.mediumPadding)
    ) {
        HSTopAppBar(
            title = { Text("Categories") },
            actions = {
                FilledTonalButton(
                    onClick = onAddCategory,

                    ) {
                    Text("Add category")
                    Icon(Icons.Default.Add, "add category")
                }
                SmallSpacer()
                FilledTonalIconButton(
                    onClick = onSettings
                ) {
                    Icon(AppIcons.SETTINGS, null)
                }
            },
            navigationIcon = {
                FilledTonalIconButton(
                    onClick = onBack
                ) {
                    Icon(AppIcons.BACK, null)
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
                    state.categories.forEachIndexed { index, category ->
                        CategoryItem(
                            modifier = Modifier
                                .widthIn(max = LocalDimensions.current.maxSurfaceWidth),
                            category = category,
                            onRemove = {
                                onAction(CategoriesListAction.OnCategoryRemove(it))
                            },
                            onUpdate = {

                            },
                            update = false,
                            onCategoryNameChange = {
                                onAction(CategoriesListAction.OnCategoryNameChange(index, it))
                            }
                        )
                    }
                }
            }
        }
    }
}



data class CategoriesListState(
    val isLoading: Boolean = true,
    val categories: List<Category> = emptyList(),
    val selectedCategoriesIds: Set<String> = emptySet()
)

sealed class CategoriesListAction {
    data class OnCategoryNameChange(val index: Int, val nameChange: String): CategoriesListAction()
    data class OnCategoryRemove(val categoryId: String): CategoriesListAction()
    data class OnCategoryUpdate(val categoryId: String): CategoriesListAction()
}

@Composable
fun CategoryItem(
    category: Category,
    onRemove: (String) -> Unit,
    onUpdate: (String) -> Unit,
    onCategoryNameChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    update: Boolean,
) {

    HSCard(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            BasicTextField(
                textStyle = TextStyle.Default.copy(color = MaterialTheme.colorScheme.onSurface),
                value = category.name,
                onValueChange = {
                    onCategoryNameChange(it)
                },
                modifier = Modifier
                    .widthIn(max = LocalDimensions.current.maxButtonWidth, min = 200.dp)
                    .weight(1f, true),
                maxLines = 1
            )

            IconButton(
                onClick = {
                    onRemove(category.categoryId ?: "")
                }
            ) {
                Icon(AppIcons.CLEAR, "remove category")
            }
        }
        if(update) {
            Row(
                modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        onUpdate(category.categoryId ?: "")
                    }
                ) {
                    Text("Update")
                }
            }
        }
    }
}


class CategoriesListViewModel(
    private val habitRepository: HabitRepository
): ViewModel() {

    private val _state = MutableStateFlow(CategoriesListState())
    val state = _state
        .onStart {
            fetchCategories()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    suspend private fun fetchCategories() {
        habitRepository.getCategories()
            .onSuccess { categories ->
                _state.update { it.copy(
                    categories = categories,
                    isLoading = false
                )
                }
            }
            .onError { error ->
                _state.update {
                    it.copy(
                        isLoading = false,
                    )
                }
            }
    }

    fun onAction(action: CategoriesListAction) {
        when(action) {
            is CategoriesListAction.OnCategoryNameChange -> {

            }
            is CategoriesListAction.OnCategoryRemove -> {
                viewModelScope.launch {
                    habitRepository.removeCategory(action.categoryId)
                        .onSuccess {
                            fetchCategories()
                        }
                        .onError {
                            println(it.name)
                        }
                }
            }
            is CategoriesListAction.OnCategoryUpdate -> {

            }
        }
    }
}