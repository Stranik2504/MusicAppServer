package dev.stranik.domain.usecases

import dev.stranik.data.dto.ArtistDto
import dev.stranik.domain.mapper.toDto
import dev.stranik.domain.repository.ArtistRepository

class SearchArtistsUseCase(
    private val artistRepository: ArtistRepository,
) {
    suspend operator fun invoke(
        q: String? = null,
        genre: String? = null,
        sort: String? = "popularity",
        limit: Int = 20,
        cursor: String? = null,
    ): List<ArtistDto> {
        return artistRepository.searchArtists(
            q = q,
            genre = genre,
            sort = sort,
            limit = limit,
            cursor = cursor,
        ).map { it.toDto() }
    }
}

