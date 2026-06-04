package dev.stranik.domain.repository

interface FollowedArtistsRepository {
    suspend fun getFollowedArtists(userId: Long): List<Long>
    suspend fun followArtist(userId: Long, artistId: Long): Boolean
    suspend fun unfollowArtist(userId: Long, artistId: Long): Boolean
}