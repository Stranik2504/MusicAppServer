package dev.stranik.domain.model

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class ArtistDetails(
    val id: Long,
    val name: String,
    val bio: String,
    val avatarUrl: String,
    val country: String,
    val monthlyListeners: Int,
    val genres: List<Genre>,
    val topTracks: List<ArtistTrack>,
    val albums: List<ArtistAlbum>,
    val followersCount: Int,
    val createdAt: LocalDateTime,
)

