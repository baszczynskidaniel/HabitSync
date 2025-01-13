package org.example.project.settings.presentation.language

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import habitsync.composeapp.generated.resources.Res
import habitsync.composeapp.generated.resources.language
import habitsync.composeapp.generated.resources.navigate_back
import org.example.project.core.presentation.design_system.HSCenterSurface
import org.example.project.core.presentation.ui.AppIcons
import org.example.project.settings.domain.Language
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageScreen(
    onBack: () -> Unit,
    onAction: (LanguageAction) -> Unit,
    state: LanguageState,
    modifier: Modifier = Modifier
) {
    HSCenterSurface(
        modifier = modifier,
        navigationIcon = {
            IconButton(
                onClick = onBack,
            ) {
                Icon(
                    AppIcons.BACK,
                    stringResource(Res.string.navigate_back)
                )
            }
        },
        title = {
            Text(stringResource(Res.string.language))
        },
    ) {
        Language.entries.forEach { language ->
            LanguageItem(
                language = language,
                selected = language == state.selectedLanguage,
                onClick = { onAction(LanguageAction.OnLanguageChange(language)) }
            )
        }
    }
}


