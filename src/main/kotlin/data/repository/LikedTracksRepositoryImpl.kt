package dev.stranik.data.repository

import dev.stranik.data.databases.LikedTracksTable
import dev.stranik.domain.repository.LikedTracksRepository
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class LikedTracksRepositoryImpl : LikedTracksRepository {
     override suspend fun getLikedTracks(userId: Long): List<Long> = transaction {
        LikedTracksTable
            .selectAll()
            .andWhere { LikedTracksTable.userId eq userId }
            .map { it[LikedTracksTable.trackId].value }
    }
}