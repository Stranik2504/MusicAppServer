package dev.stranik.domain.usecases

import dev.stranik.domain.repository.PlaylistRepository

class DeletePlaylistUseCase(
    private val playlistRepository: PlaylistRepository,
) {
    suspend operator fun invoke(userId: Long, playlistId: Long): Boolean {
        return playlistRepository.deletePlaylist(userId, playlistId)
    }
}

