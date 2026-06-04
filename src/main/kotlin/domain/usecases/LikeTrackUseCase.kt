package dev.stranik.domain.usecases

import dev.stranik.domain.repository.TrackRepository

class LikeTrackUseCase(
    private val trackRepository: TrackRepository,
) {
    suspend operator fun invoke(userId: Long, trackId: Long): Boolean {
        return trackRepository.likeTrack(userId, trackId)
    }
}

