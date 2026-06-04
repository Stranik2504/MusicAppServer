package dev.stranik.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class TrackRecommendationsDto(
    val tracks: List<TrackDto>,
)

