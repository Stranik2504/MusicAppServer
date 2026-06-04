package dev.stranik.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PlaylistOwner(
    val id: Long,
    val username: String,
)

