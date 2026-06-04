package dev.stranik.domain.usecases

import dev.stranik.domain.repository.FollowedArtistsRepository

class UnfollowArtistUseCase(
    private val followedArtistsRepository: FollowedArtistsRepository,
) {
    suspend operator fun invoke(userId: Long, artistId: Long): Boolean {
        return followedArtistsRepository.unfollowArtist(userId, artistId)
    }
}

