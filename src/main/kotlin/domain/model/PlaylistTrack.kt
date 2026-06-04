package dev.stranik.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PlaylistTrack(
    val id: Long,
    val trackId: Long,
    val position: Int,
)

