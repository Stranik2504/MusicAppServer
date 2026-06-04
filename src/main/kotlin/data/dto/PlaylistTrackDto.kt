package dev.stranik.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class PlaylistTrackDto(
    val id: Long,
    val trackId: Long,
    val position: Int,
)

