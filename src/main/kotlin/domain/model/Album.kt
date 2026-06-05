package dev.stranik.domain.model

import dev.stranik.data.serializers.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Album(
    val id: Long,
    val title: String,
    val artist: AlbumArtist,
    @Serializable(with = LocalDateTimeSerializer::class)
    val releaseDate: LocalDateTime,
    val tracks: List<AlbumTrack>,
    val coverUrl: String?,
    val genre: Genre,
    val albumType: String?,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime,
)

