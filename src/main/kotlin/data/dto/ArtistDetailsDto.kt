package dev.stranik.data.dto

import dev.stranik.data.serializers.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class ArtistDetailsDto(
    val id: Long,
    val name: String,
    val bio: String,
    val avatarUrl: String,
    val country: String,
    val monthlyListeners: Int,
    val genres: List<GenreDto>,
    val topTracks: List<ArtistTrackDto>,
    val albums: List<ArtistAlbumDto>,
    val followersCount: Int,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime,
)

