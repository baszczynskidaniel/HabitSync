package org.example.project.authentication.data.network

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.example.project.authentication.data.dto.UserDto
import org.example.project.core.data.network.BASE_URL
import org.example.project.core.data.network.safeCall
import org.example.project.core.domain.DataError
import org.example.project.core.domain.EmptyResult
import org.example.project.core.domain.Result

class KtorRemoteUserDataSource(
    private val httpClient: HttpClient
): RemoteUserDataSource {
    override suspend fun login(userDto: UserDto): Result<String?, DataError.Remote> {
        return safeCall {
            httpClient.post(
                urlString = "$BASE_URL/Users/login"
            ) {
                contentType(ContentType.Application.Json)
                setBody(
                    userDto.copy(
                        userId = null
                    )
                )
            }
        }
    }

    override suspend fun signUp(userDto: UserDto): EmptyResult<DataError.Remote> {
        return safeCall {
            httpClient.post(
                urlString = "$BASE_URL/Users"
            ) {
                contentType(ContentType.Application.Json)
                setBody(
                    userDto.copy(
                        userId = null
                    )
                )
            }
        }
    }
}