package dev.stranik.domain.repository

import dev.stranik.domain.model.Track

interface TrackRepository {
    suspend fun searchTracks(
        q: String? = null,
        artistId: Long? = null,
        albumId: Long? = null,
        durationMin: Int? = null,
        durationMax: Int? = null,
        limit: Int = 20,
        cursor: String? = null,
    ): List<Track>

    suspend fun findById(id: Long): Track?

    suspend fun getStreamUrl(id: Long): String?

    suspend fun likeTrack(userId: Long, trackId: Long): Boolean

    suspend fun unlikeTrack(userId: Long, trackId: Long): Boolean

    suspend fun getLikesCount(trackId: Long): Int
}

