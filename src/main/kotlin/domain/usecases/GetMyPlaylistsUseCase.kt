package dev.stranik.domain.usecases

import dev.stranik.domain.repository.PlaylistRepository

class GetMyPlaylistsUseCase(
    private val playlistRepository: PlaylistRepository,
) {
    suspend operator fun invoke(userId: Long): List<Long> {
        return playlistRepository.getAllPlaylistUser(userId)
    }
}