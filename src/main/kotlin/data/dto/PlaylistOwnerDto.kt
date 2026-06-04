package dev.stranik.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class PlaylistOwnerDto(
    val id: Long,
    val username: String,
)

