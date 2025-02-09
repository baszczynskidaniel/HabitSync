package org.example.project.habit.domain.use_cases

import org.example.project.core.domain.EmptyResult
import org.example.project.core.domain.Result
import org.example.project.habit.domain.ValidationError

class ValidateHabitName {

    fun execute(name: String): EmptyResult<ValidationError> {
        if(name.isBlank())
            return Result.Error(ValidationError.NAME_IS_EMPTY)
        return Result.Success(Unit)
    }
}