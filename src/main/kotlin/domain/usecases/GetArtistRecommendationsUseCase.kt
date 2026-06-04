package dev.stranik.domain.usecases

import dev.stranik.data.dto.TrackRecommendationsDto
import dev.stranik.domain.mapper.toDto
import dev.stranik.domain.repository.RecommendationRepository

class GetArtistRecommendationsUseCase(
    private val recommendationRepository: RecommendationRepository,
) {
    suspend operator fun invoke(artistId: Long, limit: Int = 10): TrackRecommendationsDto? {
        return recommendationRepository.getArtistRecommendations(artistId, limit)?.toDto()
    }
}

