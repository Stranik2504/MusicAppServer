package dev.stranik.domain.repository

import dev.stranik.domain.model.Playlist

interface PlaylistRepository {
    suspend fun searchPlaylists(
        userId: Long? = null,
        q: String? = null,
        publicOnly: Boolean? = null,
        limit: Int = 20,
        cursor: String? = null,
    ): List<Playlist>

    suspend fun findById(id: Long): Playlist?

    suspend fun createPlaylist(userId: Long, title: String, description: String?, isPublic: Boolean, coverUrl: String?): Playlist?

    suspend fun updatePlaylist(userId: Long, playlistId: Long, title: String?, description: String?, isPublic: Boolean?): Boolean

    suspend fun deletePlaylist(userId: Long, playlistId: Long): Boolean

    suspend fun addTrack(playlistId: Long, trackId: Long, position: Int?): Boolean

    suspend fun removeTrack(playlistId: Long, trackId: Long): Boolean

    suspend fun getAllPlaylistUser(userId: Long): List<Long>
}

