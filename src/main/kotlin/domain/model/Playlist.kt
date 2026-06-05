package dev.stranik.domain.model

import dev.stranik.data.serializers.LocalDateTimeSerializer
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
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    val updatedAt: LocalDateTime,
)

