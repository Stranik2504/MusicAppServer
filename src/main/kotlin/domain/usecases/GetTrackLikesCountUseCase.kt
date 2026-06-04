package dev.stranik.domain.usecases

import dev.stranik.domain.repository.TrackRepository

class GetTrackLikesCountUseCase(
    private val trackRepository: TrackRepository,
) {
    suspend operator fun invoke(trackId: Long): Int {
        return trackRepository.getLikesCount(trackId)
    }
}

