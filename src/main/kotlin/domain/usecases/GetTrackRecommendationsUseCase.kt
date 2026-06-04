package dev.stranik.domain.usecases

import dev.stranik.data.dto.TrackRecommendationsDto
import dev.stranik.domain.mapper.toDto
import dev.stranik.domain.repository.RecommendationRepository

class GetTrackRecommendationsUseCase(
    private val recommendationRepository: RecommendationRepository,
) {
    suspend operator fun invoke(trackId: Long, limit: Int = 10): TrackRecommendationsDto? {
        return recommendationRepository.getTrackRecommendations(trackId, limit)?.toDto()
    }
}

