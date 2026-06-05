package dev.stranik.data.dto

import dev.stranik.data.serializers.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class AlbumDto(
    val id: Long,
    val title: String,
    val artist: AlbumArtistDto,
    @Serializable(with = LocalDateTimeSerializer::class)
    val releaseDate: LocalDateTime,
    val tracks: List<AlbumTrackDto>,
    val coverUrl: String?,
    val genre: GenreDto,
    val albumType: String?,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime,
)
