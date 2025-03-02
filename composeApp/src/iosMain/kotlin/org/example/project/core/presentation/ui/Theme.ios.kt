package org.example.project.core.presentation.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import org.example.project.settings.domain.Language

@Composable
actual fun AppTheme(
    language: Language,
    darkTheme: Boolean,
    dynamicColor: Boolean,
    content: @Composable () -> Unit,

    ) {
    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val dimensions: Dimensions = Dimensions()

    CompositionLocalProvider(
        LocalDimensions provides dimensions
    ) {

        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}