package dev.stranik.domain.usecases

import dev.stranik.data.dto.PlaylistDto
import dev.stranik.domain.mapper.toDto
import dev.stranik.domain.repository.PlaylistRepository

class CreatePlaylistUseCase(
    private val playlistRepository: PlaylistRepository,
) {
    suspend operator fun invoke(userId: Long, title: String, description: String?, isPublic: Boolean = true, coverUrl: String? = null): PlaylistDto? {
        return playlistRepository.createPlaylist(userId, title, description, isPublic, coverUrl)?.toDto()
    }
}

