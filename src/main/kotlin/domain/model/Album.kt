package dev.stranik.domain.model

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Album(
    val id: Long,
    val title: String,
    val artist: AlbumArtist,
    val releaseDate: LocalDateTime,
    val tracks: List<AlbumTrack>,
    val coverUrl: String?,
    val genre: Genre,
    val albumType: String?,
    val createdAt: LocalDateTime,
)

