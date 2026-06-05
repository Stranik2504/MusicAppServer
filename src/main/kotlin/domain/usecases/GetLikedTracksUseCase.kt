package dev.stranik.domain.usecases

import dev.stranik.domain.repository.LikedTracksRepository

class GetLikedTracksUseCase(
    private val trackRepository: LikedTracksRepository,
) {
    suspend operator fun invoke(userId: Long): List<Long> {
        return trackRepository.getLikedTracks(userId)
    }
}