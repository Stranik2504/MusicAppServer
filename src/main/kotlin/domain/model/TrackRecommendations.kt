package dev.stranik.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TrackRecommendations(
    val tracks: List<Track>,
)

