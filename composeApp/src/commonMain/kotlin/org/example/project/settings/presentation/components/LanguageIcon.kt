package org.example.project.settings.presentation.components

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import habitsync.composeapp.generated.resources.Res
import habitsync.composeapp.generated.resources.flag_great_britain
import habitsync.composeapp.generated.resources.flag_poland

import org.example.project.settings.domain.Language
import org.jetbrains.compose.resources.vectorResource

@Composable
fun LanguageIcon(
    language: Language,
    modifier: Modifier = Modifier,
) {
    val icon = when(language) {
        Language.POLISH -> vectorResource(Res.drawable.flag_poland)
        Language.ENGLISH -> vectorResource(Res.drawable.flag_great_britain)
    }
    Icon(modifier = modifier,
        imageVector = icon,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
