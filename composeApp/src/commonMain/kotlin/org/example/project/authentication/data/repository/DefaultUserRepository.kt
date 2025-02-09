package org.example.project.authentication.data.repository

import org.example.project.authentication.data.dto.UserDto
import org.example.project.authentication.data.network.RemoteUserDataSource
import org.example.project.authentication.domain.User
import org.example.project.authentication.domain.UserRepository
import org.example.project.core.domain.DataError
import org.example.project.core.domain.EmptyResult
import org.example.project.core.domain.Result

class DefaultUserRepository(
    private val remoteUserDataSource: RemoteUserDataSource
): UserRepository {
    override suspend fun login(user: User): Result<String?, DataError.Remote> {
       return remoteUserDataSource
           .login(UserDto(
               userId = null,
               username = user.username,
               password = user.password,
           ))
    }

    override suspend fun signUp(user: User): EmptyResult<DataError.Remote> {
        return remoteUserDataSource
            .signUp(UserDto(
                userId = null,
                username = user.username,
                password = user.password,
            ))
    }
}