package dev.stranik.data.dto

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class AlbumDto(
    val id: Long,
    val title: String,
    val artist: AlbumArtistDto,
    val releaseDate: LocalDateTime,
    val tracks: List<AlbumTrackDto>,
    val coverUrl: String?,
    val genre: GenreDto,
    val albumType: String?,
    val createdAt: LocalDateTime,
)
