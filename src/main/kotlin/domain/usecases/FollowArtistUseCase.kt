package dev.stranik.domain.usecases

import dev.stranik.domain.repository.ArtistRepository
import dev.stranik.domain.repository.FollowedArtistsRepository

class FollowArtistUseCase(
    private val artistRepository: ArtistRepository,
    private val followedArtistsRepository: FollowedArtistsRepository,
) {
    suspend operator fun invoke(userId: Long, artistId: Long): Boolean {
        if (artistRepository.findById(artistId) == null) {
            return false
        }

        return followedArtistsRepository.followArtist(userId, artistId)
    }
}

