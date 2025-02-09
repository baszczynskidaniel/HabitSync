package org.example.project.habit.data.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.utils.io.InternalAPI
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.example.project.core.data.datastore.MIUPreferencesDataSource
import org.example.project.core.data.network.BASE_URL
import org.example.project.core.data.network.safeCall
import org.example.project.core.domain.DataError
import org.example.project.core.domain.EmptyResult
import org.example.project.core.domain.Result
import org.example.project.habit.data.dto.CategoryDto
import org.example.project.habit.data.dto.CategoryHabitDto
import org.example.project.habit.data.dto.HabitDto
import org.example.project.habit.data.dto.UpdateHabitScoreDto
import org.example.project.habit.domain.Category
import org.example.project.habit.domain.Habit




class KtorRemoteHabitDataSource(
    private val httpClient: HttpClient,
    private val miuPreferencesDataSource: MIUPreferencesDataSource
): RemoteHabitDataSource {
    init {
        runBlocking {
            userId = miuPreferencesDataSource
                .userData
                .first()
                .userId
                .removePrefix("\"")
                .removeSuffix("\"")
        }
    }
    private lateinit var userId: String

    private fun getUserId(): String {
        var userId: String  = ""
        runBlocking {
            userId = miuPreferencesDataSource
                .userData
                .first()
                .userId
                .removePrefix("\"")
                .removeSuffix("\"")
        }
        return userId
    }

    override suspend fun getCategories(): Result<List<CategoryDto>, DataError.Remote> {
        val userId = getUserId()
        return safeCall {
            httpClient.get(
                urlString = "$BASE_URL/Categories/{$userId}"
            ) {

            }
        }
    }

    override suspend fun postCategory(category: Category): EmptyResult<DataError.Remote> {
        val userId = getUserId()
        val dto = CategoryDto(
            categoryId = null,
            userId = userId,
            name = category.name
        )
        return safeCall {
            httpClient.post(
                urlString = "$BASE_URL/Categories/createCategory"
            ) {
                contentType(ContentType.Application.Json)
                setBody(dto)
            }
        }
    }

    override suspend fun removeCategory(categoryId: String): EmptyResult<DataError.Remote> {

        return safeCall {
            httpClient.delete(
                urlString = "$BASE_URL/Categories/{$categoryId}"
            ) {

            }
        }
    }



    override suspend fun getHabitById(habitId: String): Result<HabitDto, DataError.Remote> {
        return safeCall {
            httpClient.get(
                urlString = "$BASE_URL/Habits/{$habitId}"
            ) {

            }
        }
    }

    override suspend fun updateHabitScore(
        updateHabitScoreDto: UpdateHabitScoreDto
    ): EmptyResult<DataError.Remote> {
        val result: EmptyResult<DataError.Remote> = safeCall {
            try {
                val response = httpClient.post(
                    urlString = "$BASE_URL/Habits/updateHabitScore"
                ) {
                    contentType(ContentType.Application.Json)
                    setBody(updateHabitScoreDto)



                }
                println("Successfully posted habit: $response")
                response
            }
            catch (e: Exception) {
                println("Failed to post habit: ${e.message}")
                throw e
            }

        }
        println("Post habit result: $result")
        return result
    }

    @OptIn(InternalAPI::class)
    override suspend fun getHabits(): Result<List<HabitDto>, DataError.Remote> {
        runBlocking {
            userId = miuPreferencesDataSource
                .userData
                .first()
                .userId
                .removePrefix("\"")
                .removeSuffix("\"")
        }
        val a =  safeCall<List<HabitDto>> {
            try {
            val response = httpClient.get(
                urlString = "$BASE_URL/Habits/user/{$userId}"
            ) {

            }
                println(response)
                response

            }  catch (e: Exception) {
                println("Failed to post habit: ${e.message}")
                throw e
            }
        }
        println(a)
        println("b")
        return a
    }

    override suspend fun removeHabitById(habitId: String): EmptyResult<DataError.Remote> {
        return safeCall {
            try {
                val response = httpClient.delete(
                    urlString = "$BASE_URL/Habits/$habitId"
                ) {

                }
                println(response)
                response

            }
            catch (e: Exception) {
                println("Failed to post habit: ${e.message}")
                throw e
            }
        }
    }

    @OptIn(InternalAPI::class)
    override suspend fun postHabit(habit: Habit): EmptyResult<DataError.Remote> {
        val result: EmptyResult<DataError.Remote> = safeCall {
            try {
                val payload = HabitDto(
                    habitId = null,
                    userId = userId,
                    unit = habit.unit,
                    name = habit.name,
                    description = habit.description,
                    isPositive = habit.isPositive,
                    isMeasurable = habit.isMeasurable,
                    progressPercentage = 0.0,
                    target = habit.target,
                    hasTarget = habit.hasTarget,
                    count = 0.0,

                )
                println("Payload to be sent: ${Json.encodeToString(payload)}") // Serialized payload



                val response = httpClient.post(
                    urlString = "$BASE_URL/Habits"
                ) {
                    contentType(ContentType.Application.Json)
                    setBody(HabitDto(
                        habitId = null,
                        userId = userId,
                        unit = habit.unit,
                        name = habit.name,
                        description = habit.description,
                        isPositive = habit.isPositive,
                        isMeasurable = habit.isMeasurable,
                        progressPercentage = 0.0,
                        target = habit.target,
                        hasTarget = habit.hasTarget,
                        count = 0.0,

                    )
                    )
                    println("a")

                }
                println("Successfully posted habit: $response")
                response
            } catch (e: Exception) {
                println("Failed to post habit: ${e.message}")
                throw e
            }
        }
        println("Post habit result: $result")
        return result
    }

    override suspend fun attachHabitCategory(categoryHabitDto: CategoryHabitDto): EmptyResult<DataError.Remote> {
        return safeCall {
            try {
                val response = httpClient.post(
                    urlString = "$BASE_URL/CategoryHabit/attach"
                ) {
                    contentType(ContentType.Application.Json)
                    setBody(categoryHabitDto)
                }
                println("%%%%% $response")
                response
            } catch (e: Exception) {
                println("Failed to post habit: ${e.message}")
                throw e
            }
        }
    }

    override suspend fun detachHabitCategory(categoryHabitDto: CategoryHabitDto): EmptyResult<DataError.Remote> {
        return safeCall {
            try {
            val response = httpClient.post(
                urlString = "$BASE_URL/CategoryHabit/detach"
            ) {
                contentType(ContentType.Application.Json)
                setBody(categoryHabitDto)
            }
            println("Successfully posted habit: $response")
            response
            } catch (e: Exception) {
                println("Failed to post habit: ${e.message}")
                throw e
            }

        }
    }

    override suspend fun getHabitCategories(habitId: String): Result<List<CategoryDto>, DataError.Remote> {
        return safeCall {
            try {
                val response = httpClient.post(

                urlString = "$BASE_URL/Habits/habitCategories/$habitId"
            ) {

            }
                println("Successfully posted habit: $response")
                response
            } catch (e: Exception) {
                println("Failed to post habit: ${e.message}")
                throw e
            }
        }
    }
}