package dev.stranik.data.dto

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class ArtistDto(
    val name: String,
    val bio: String,
    val avatarUrl: String,
    val country: String,
    val monthlyListeners: Int,
    val createdAt: LocalDateTime,
)

