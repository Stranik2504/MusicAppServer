package dev.stranik.domain.model

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class History(
    val id: Long,
    val userId: Long,
    val trackId: Long,
    val playedSec: Int,
    val playedAt: LocalDateTime
)