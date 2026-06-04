package dev.stranik.domain.usecases

import dev.stranik.data.dto.ArtistDetailsDto
import dev.stranik.domain.mapper.toDto
import dev.stranik.domain.repository.ArtistRepository

class GetArtistUseCase(
    private val artistRepository: ArtistRepository,
) {
    suspend operator fun invoke(artistId: Long): ArtistDetailsDto? {
        return artistRepository.getDetails(artistId)?.toDto()
    }
}

