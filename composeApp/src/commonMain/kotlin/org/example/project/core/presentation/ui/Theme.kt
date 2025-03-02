package org.example.project.core.presentation.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import org.example.project.core.presentation.ui.Pink40
import org.example.project.core.presentation.ui.Pink80
import org.example.project.core.presentation.ui.Purple40
import org.example.project.core.presentation.ui.Purple80
import org.example.project.core.presentation.ui.PurpleGrey40
import org.example.project.core.presentation.ui.PurpleGrey80
import org.example.project.settings.domain.Language


val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)
val LocalLocalization = staticCompositionLocalOf { Language.ENGLISH.toString() }
@Composable
expect fun AppTheme(
    language: Language,
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
)

