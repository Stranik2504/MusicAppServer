package dev.stranik.data.dto

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class PlaylistDto(
    val id: Long,
    val title: String,
    val description: String?,
    val owner: PlaylistOwnerDto,
    val tracks: List<PlaylistTrackDto>,
    val isPublic: Boolean,
    val coverUrl: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)

