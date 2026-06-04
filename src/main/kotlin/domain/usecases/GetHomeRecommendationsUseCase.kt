package dev.stranik.domain.usecases

import dev.stranik.data.dto.HomeRecommendationsDto
import dev.stranik.domain.mapper.toDto
import dev.stranik.domain.repository.RecommendationRepository

class GetHomeRecommendationsUseCase(
    private val recommendationRepository: RecommendationRepository,
) {
    suspend operator fun invoke(userId: Long, limit: Int = 10): HomeRecommendationsDto {
        return recommendationRepository.getHomeRecommendations(userId, limit).toDto()
    }
}

