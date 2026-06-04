package dev.stranik.domain.usecases

import dev.stranik.data.dto.PlaylistDto
import dev.stranik.domain.mapper.toDto
import dev.stranik.domain.repository.PlaylistRepository

class SearchPlaylistsUseCase(
    private val playlistRepository: PlaylistRepository,
) {
    suspend operator fun invoke(
        userId: Long? = null,
        q: String? = null,
        publicOnly: Boolean? = null,
        limit: Int = 20,
        cursor: String? = null,
    ): List<PlaylistDto> {
        return playlistRepository.searchPlaylists(userId = userId, q = q, publicOnly = publicOnly, limit = limit, cursor = cursor).map { it.toDto() }
    }
}

