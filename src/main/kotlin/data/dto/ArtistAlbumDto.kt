package dev.stranik.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ArtistAlbumDto(
    val id: Long,
    val title: String,
)