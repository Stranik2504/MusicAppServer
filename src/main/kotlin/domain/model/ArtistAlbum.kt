package dev.stranik.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ArtistAlbum(
    val id: Long,
    val title: String,
)