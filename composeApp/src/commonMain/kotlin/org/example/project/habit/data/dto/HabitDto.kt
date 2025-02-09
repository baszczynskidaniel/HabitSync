package org.example.project.habit.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder


@Serializable
data class HabitDto(
    @SerialName("habitId") val habitId: String?,
    @SerialName("userId") val userId: String,
    @SerialName("name") val name: String,
    @SerialName("description") val description: String?,
    @SerialName("unit") val unit: String?,
    @SerialName("isPositive") val isPositive: Boolean,
    @SerialName("isMeasurable") val isMeasurable: Boolean,
    @SerialName("progressPercentage") val progressPercentage: Double?,
    @SerialName("target") val target: Double,
    @SerialName("hasTarget") val hasTarget: Boolean,
    @SerialName("count") val count: Double,

)



