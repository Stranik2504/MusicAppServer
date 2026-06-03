package dev.stranik.data.repository

import dev.stranik.domain.repository.FollowedArtistsRepository

class FollowedArtistsRepositoryImpl : FollowedArtistsRepository {
    override suspend fun getFollowedArtists(userId: Long): List<Long> {
        TODO("Not yet implemented")
    }
}