package dev.stranik.data.dto

import dev.stranik.data.serializers.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class HistoryInfoDto(
    val trackId: Long,
    val playedSec: Int,
    @Serializable(with = LocalDateTimeSerializer::class)
    val playedAt: LocalDateTime
)