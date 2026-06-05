package dev.stranik.domain.model

import dev.stranik.data.serializers.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Track(
    val id: Long,
    val title: String,
    val artist: AlbumArtist,
    val album: AlbumArtist?,
    val durationSec: Int,
    val playCount: Int,
    val fileUrl: String,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime,
)

