package org.example.project.authentication.domain

import org.example.project.authentication.data.dto.UserDto
import org.example.project.core.domain.DataError
import org.example.project.core.domain.EmptyResult
import org.example.project.core.domain.Result

interface UserRepository {
    suspend fun signUp(user: User): EmptyResult<DataError.Remote>
    suspend fun login(user: User): Result<String?, DataError.Remote>
}