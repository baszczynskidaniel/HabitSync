package org.example.project.core.data

import org.example.project.settings.domain.Language

actual fun setAppLanguage(language: Language) {
    val locale = java.util.Locale(language.toString())
    java.util.Locale.setDefault(locale)
}