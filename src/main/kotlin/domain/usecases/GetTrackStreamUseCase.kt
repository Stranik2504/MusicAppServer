package dev.stranik.domain.usecases

import dev.stranik.domain.repository.TrackRepository

class GetTrackStreamUseCase(
    private val trackRepository: TrackRepository,
) {
    suspend operator fun invoke(trackId: Long): String? {
        return trackRepository.getStreamUrl(trackId)
    }
}

