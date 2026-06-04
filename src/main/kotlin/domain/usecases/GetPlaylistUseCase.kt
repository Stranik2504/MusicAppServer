package dev.stranik.domain.usecases

import dev.stranik.data.dto.PlaylistDto
import dev.stranik.domain.mapper.toDto
import dev.stranik.domain.repository.PlaylistRepository

class GetPlaylistUseCase(
    private val playlistRepository: PlaylistRepository,
) {
    suspend operator fun invoke(playlistId: Long): PlaylistDto? {
        return playlistRepository.findById(playlistId)?.toDto()
    }
}

