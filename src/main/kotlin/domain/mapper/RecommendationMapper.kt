package dev.stranik.domain.mapper

import dev.stranik.data.dto.HomeRecommendationsDto
import dev.stranik.data.dto.TrackRecommendationsDto
import dev.stranik.domain.model.HomeRecommendations
import dev.stranik.domain.model.TrackRecommendations

fun HomeRecommendations.toDto() = HomeRecommendationsDto(
    albums = albums.map { it.toDto() },
    tracks = tracks.map { it.toDto() },
)

fun TrackRecommendations.toDto() = TrackRecommendationsDto(
    tracks = tracks.map { it.toDto() },
)

