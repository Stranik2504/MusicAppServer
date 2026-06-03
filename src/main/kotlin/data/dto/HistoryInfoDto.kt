package dev.stranik.data.dto

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class HistoryInfoDto(
    val trackId: Long,
    val playedSec: Int,
    val playedAt: LocalDateTime
)