package dev.stranik.domain.usecases

import dev.stranik.domain.repository.PlaylistRepository

class UpdatePlaylistUseCase(
    private val playlistRepository: PlaylistRepository,
) {
    suspend operator fun invoke(userId: Long, playlistId: Long, title: String?, description: String?, isPublic: Boolean?): Boolean {
        return playlistRepository.updatePlaylist(userId, playlistId, title, description, isPublic)
    }
}

