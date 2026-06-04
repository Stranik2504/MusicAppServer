package dev.stranik.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdatePlaylistRequestDto(
    val title: String? = null,
    val description: String? = null,
    val isPublic: Boolean? = null,
)

