package dev.stranik.domain.repository

interface LikedTracksRepository {
    suspend fun getLikedTracks(userId: Long): List<Long>
}