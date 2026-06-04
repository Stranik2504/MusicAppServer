package dev.stranik.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AlbumTrack(
    val id: Long,
    val title: String,
    val durationSec: Int,
    val trackNumber: Int,
)