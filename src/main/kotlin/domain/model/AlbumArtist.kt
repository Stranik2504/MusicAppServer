package dev.stranik.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AlbumArtist(
    val id: Long,
    val name: String,
)