package dev.stranik.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AlbumArtistDto(
    val id: Long,
    val name: String,
)