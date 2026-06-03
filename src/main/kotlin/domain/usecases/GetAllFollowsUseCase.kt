package dev.stranik.domain.usecases

import dev.stranik.domain.repository.FollowedArtistsRepository

class GetAllFollowsUseCase(
    private val followedArtistsRepository: FollowedArtistsRepository
) {
    suspend operator fun invoke(userId: Long) = followedArtistsRepository.getFollowedArtists(userId)
}