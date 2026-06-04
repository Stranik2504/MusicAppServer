package dev.stranik.domain.usecases

import dev.stranik.data.dto.AlbumDto
import dev.stranik.domain.mapper.toDto
import dev.stranik.domain.repository.AlbumRepository

class GetAlbumUseCase(
    private val albumRepository: AlbumRepository,
) {
    suspend operator fun invoke(albumId: Long): AlbumDto? {
        return albumRepository.findById(albumId)?.toDto()
    }
}

