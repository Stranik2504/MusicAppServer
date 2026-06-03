package dev.stranik.domain.repository

interface FollowedArtistsRepository {
    suspend fun getFollowedArtists(userId: Long): List<Long>
}