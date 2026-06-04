package dev.stranik.domain.usecases

import dev.stranik.data.dto.TrackDto
import dev.stranik.domain.mapper.toDto
import dev.stranik.domain.repository.TrackRepository

class SearchTracksUseCase(
    private val trackRepository: TrackRepository,
) {
    suspend operator fun invoke(
        q: String? = null,
        artistId: Long? = null,
        albumId: Long? = null,
        durationMin: Int? = null,
        durationMax: Int? = null,
        limit: Int = 20,
        cursor: String? = null,
    ): List<TrackDto> {
        return trackRepository.searchTracks(q, artistId, albumId, durationMin, durationMax, limit, cursor)
            .map { it.toDto() }
    }
}

