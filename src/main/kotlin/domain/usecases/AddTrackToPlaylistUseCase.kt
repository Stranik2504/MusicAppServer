package dev.stranik.domain.usecases

import dev.stranik.domain.repository.PlaylistRepository

class AddTrackToPlaylistUseCase(
    private val playlistRepository: PlaylistRepository,
) {
    suspend operator fun invoke(playlistId: Long, trackId: Long, position: Int?): Boolean {
        return playlistRepository.addTrack(playlistId, trackId, position)
    }
}

