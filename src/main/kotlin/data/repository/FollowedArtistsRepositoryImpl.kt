package dev.stranik.data.repository

import dev.stranik.data.databases.FollowedArtistsTable
import dev.stranik.domain.repository.FollowedArtistsRepository
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class FollowedArtistsRepositoryImpl : FollowedArtistsRepository {
    override suspend fun getFollowedArtists(userId: Long): List<Long> = transaction {
        FollowedArtistsTable
            .selectAll()
            .where { FollowedArtistsTable.userId eq userId }
            .map { it[FollowedArtistsTable.artistId].value }
    }

    override suspend fun followArtist(userId: Long, artistId: Long): Boolean = transaction {
        val exists = FollowedArtistsTable
            .selectAll()
            .where { (FollowedArtistsTable.userId eq userId) and (FollowedArtistsTable.artistId eq artistId) }
            .any()

        if (exists) {
            return@transaction true
        }

        FollowedArtistsTable.insert {
            it[FollowedArtistsTable.userId] = userId
            it[FollowedArtistsTable.artistId] = artistId
        }

        true
    }

    override suspend fun unfollowArtist(userId: Long, artistId: Long): Boolean = transaction {
        FollowedArtistsTable.deleteWhere {
            (FollowedArtistsTable.userId eq userId) and (FollowedArtistsTable.artistId eq artistId)
        } > 0
    }
}