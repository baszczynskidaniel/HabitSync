package org.example.project.authentication.data.network

import org.example.project.authentication.data.dto.UserDto
import org.example.project.core.domain.DataError
import org.example.project.core.domain.EmptyResult
import org.example.project.core.domain.Result

interface RemoteUserDataSource {
    suspend fun login(userDto: UserDto): Result<String?, DataError.Remote>
    suspend fun signUp(userDto: UserDto): EmptyResult<DataError.Remote>
}