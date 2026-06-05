package dev.stranik.domain.model

import dev.stranik.data.serializers.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class History(
    val id: Long,
    val userId: Long,
    val trackId: Long,
    val playedSec: Int,
    @Serializable(with = LocalDateTimeSerializer::class)
    val playedAt: LocalDateTime
)