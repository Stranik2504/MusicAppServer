package dev.stranik.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AddTrackRequestDto(
    val trackId: Long,
    val position: Int? = null,
)

