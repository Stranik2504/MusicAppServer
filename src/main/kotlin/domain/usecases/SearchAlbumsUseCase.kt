package dev.stranik.domain.usecases

import dev.stranik.data.dto.AlbumDto
import dev.stranik.domain.mapper.toDto
import dev.stranik.domain.repository.AlbumRepository

class SearchAlbumsUseCase(
    private val albumRepository: AlbumRepository,
) {
    suspend operator fun invoke(
        artistId: Long? = null,
        q: String? = null,
        year: Int? = null,
        limit: Int = 20,
        cursor: String? = null,
    ): List<AlbumDto> {
        return albumRepository.searchAlbums(
            artistId = artistId,
            q = q,
            year = year,
            limit = limit,
            cursor = cursor,
        ).map { it.toDto() }
    }
}

