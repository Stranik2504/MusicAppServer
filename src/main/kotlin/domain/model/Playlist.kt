package dev.stranik.domain.model

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Playlist(
    val id: Long,
    val title: String,
    val description: String?,
    val owner: PlaylistOwner,
    val tracks: List<PlaylistTrack>,
    val isPublic: Boolean,
    val coverUrl: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)

