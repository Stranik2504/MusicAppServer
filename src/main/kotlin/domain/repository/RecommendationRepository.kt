package dev.stranik.domain.repository

import dev.stranik.domain.model.HomeRecommendations
import dev.stranik.domain.model.TrackRecommendations

interface RecommendationRepository {
    suspend fun getHomeRecommendations(userId: Long, limit: Int = 10): HomeRecommendations
    suspend fun getTrackRecommendations(trackId: Long, limit: Int = 10): TrackRecommendations?
    suspend fun getArtistRecommendations(artistId: Long, limit: Int = 10): TrackRecommendations?
}

