package dev.stranik.domain.usecases

import dev.stranik.data.dto.TrackDto
import dev.stranik.domain.mapper.toDto
import dev.stranik.domain.repository.TrackRepository

class GetTrackUseCase(
    private val trackRepository: TrackRepository,
) {
    suspend operator fun invoke(trackId: Long): TrackDto? {
        return trackRepository.findById(trackId)?.toDto()
    }
}

