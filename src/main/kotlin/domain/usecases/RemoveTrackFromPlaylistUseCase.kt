package dev.stranik.domain.usecases

import dev.stranik.domain.repository.PlaylistRepository

class RemoveTrackFromPlaylistUseCase(
    private val playlistRepository: PlaylistRepository,
) {
    suspend operator fun invoke(playlistId: Long, trackId: Long): Boolean {
        return playlistRepository.removeTrack(playlistId, trackId)
    }
}

