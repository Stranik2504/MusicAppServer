package dev.stranik.domain.model

import dev.stranik.data.serializers.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Artist(
    val id: Long,
    val name: String,
    val bio: String,
    val avatarUrl: String,
    val country: String,
    val monthlyListeners: Int,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime,
)

