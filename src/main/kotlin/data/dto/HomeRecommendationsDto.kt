package dev.stranik.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class HomeRecommendationsDto(
    val albums: List<AlbumDto>,
    val tracks: List<TrackDto>,
)

