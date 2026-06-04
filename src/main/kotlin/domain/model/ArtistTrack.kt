package dev.stranik.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ArtistTrack(
    val id: Long,
    val title: String,
    val durationSec: Int,
)

