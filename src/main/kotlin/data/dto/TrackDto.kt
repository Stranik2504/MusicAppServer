package dev.stranik.data.dto

import dev.stranik.data.serializers.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class TrackDto(
    val id: Long,
    val title: String,
    val artist: AlbumArtistDto,
    val album: AlbumArtistDto?,
    val durationSec: Int,
    val playCount: Int,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime,
)

