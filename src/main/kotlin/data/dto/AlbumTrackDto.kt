package dev.stranik.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AlbumTrackDto(
    val id: Long,
    val title: String,
    val durationSec: Int,
    val trackNumber: Int,
)