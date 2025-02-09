package org.example.project.habit.domain

import org.example.project.core.domain.DataError
import org.example.project.core.domain.Error

enum class ValidationError: Error {
    NAME_IS_EMPTY
}