package dev.stranik.data.repository

import dev.stranik.data.databases.PlaylistTracksTable
import dev.stranik.data.databases.PlaylistsTable
import dev.stranik.data.databases.UserTable
import dev.stranik.domain.model.Playlist
import dev.stranik.domain.model.PlaylistOwner
import dev.stranik.domain.model.PlaylistTrack
import dev.stranik.domain.repository.PlaylistRepository
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.update

class PlaylistRepositoryImpl : PlaylistRepository {
    override suspend fun searchPlaylists(
        userId: Long?,
        q: String?,
        publicOnly: Boolean?,
        limit: Int,
        cursor: String?,
    ): List<Playlist> = transaction {
        val query = PlaylistsTable
            .join(UserTable, JoinType.INNER, PlaylistsTable.userId, UserTable.id)
            .selectAll()
            .apply {
                if (userId != null) andWhere { PlaylistsTable.userId eq userId }
                if (!q.isNullOrBlank()) andWhere { PlaylistsTable.title like "%$q%" }
                if (publicOnly == true) andWhere { PlaylistsTable.isPublic eq true }
                if (!cursor.isNullOrBlank()) {
                    cursor.toLongOrNull()?.let { andWhere { PlaylistsTable.id greater it } }
                }
                orderBy(PlaylistsTable.id to SortOrder.ASC)
                limit(limit)
            }

        query.map { row -> row.toPlaylist() }
    }

    override suspend fun findById(id: Long): Playlist? = transaction {
        findByIdInternal(id)
    }

    override suspend fun createPlaylist(userId: Long, title: String, description: String?, isPublic: Boolean, coverUrl: String?): Playlist? = transaction {
        PlaylistsTable.insert {
            it[PlaylistsTable.userId] = userId
            it[PlaylistsTable.title] = title
            it[PlaylistsTable.coverUrl] = coverUrl.orEmpty()
            it[PlaylistsTable.isPublic] = isPublic
        }

        findByIdInternal(PlaylistsTable.selectAll().orderBy(PlaylistsTable.id to SortOrder.DESC).limit(1).single()[PlaylistsTable.id].value)
    }

    override suspend fun updatePlaylist(userId: Long, playlistId: Long, title: String?, description: String?, isPublic: Boolean?): Boolean = transaction {
        val exists = PlaylistsTable.selectAll().where { PlaylistsTable.id eq playlistId }.singleOrNull() ?: return@transaction false
        if (exists[PlaylistsTable.userId].value != userId) return@transaction false

        PlaylistsTable.update({ PlaylistsTable.id eq playlistId }) {
            if (title != null) it[PlaylistsTable.title] = title
            if (isPublic != null) it[PlaylistsTable.isPublic] = isPublic
        }

        true
    }

    override suspend fun deletePlaylist(userId: Long, playlistId: Long): Boolean = transaction {
        val deleted = PlaylistsTable.deleteWhere { (PlaylistsTable.id eq playlistId) and (PlaylistsTable.userId eq userId) }
        deleted > 0
    }

    override suspend fun addTrack(playlistId: Long, trackId: Long, position: Int?): Boolean = transaction {
        val lastPosition = PlaylistTracksTable.selectAll()
            .andWhere { PlaylistTracksTable.playlistId eq playlistId }
            .orderBy(PlaylistTracksTable.position to SortOrder.DESC)
            .limit(1)
            .singleOrNull()
            ?.get(PlaylistTracksTable.position) ?: 0

        val pos = position ?: (lastPosition + 1)

        PlaylistTracksTable.insertIgnore {
            it[PlaylistTracksTable.playlistId] = playlistId
            it[PlaylistTracksTable.trackId] = trackId
            it[PlaylistTracksTable.position] = pos
        }

        true
    }

    override suspend fun removeTrack(playlistId: Long, trackId: Long): Boolean = transaction {
        val deleted = PlaylistTracksTable.deleteWhere { (PlaylistTracksTable.playlistId eq playlistId) and (PlaylistTracksTable.trackId eq trackId) }
        deleted > 0
    }

    private fun findByIdInternal(id: Long): Playlist? {
        val row = PlaylistsTable
            .join(UserTable, JoinType.INNER, PlaylistsTable.userId, UserTable.id)
            .selectAll()
            .where { PlaylistsTable.id eq id }
            .singleOrNull() ?: return null

        return row.toPlaylist()
    }

    private fun ResultRow.toPlaylist(): Playlist {
        val playlistId = this[PlaylistsTable.id].value

        val owner = PlaylistOwner(
            id = this[UserTable.id].value,
            username = this[UserTable.username],
        )

        val tracks = PlaylistTracksTable
            .selectAll()
            .where { PlaylistTracksTable.playlistId eq playlistId }
            .orderBy(PlaylistTracksTable.position to SortOrder.ASC)
            .map { trackRow ->
                PlaylistTrack(
                    id = trackRow[PlaylistTracksTable.id].value,
                    trackId = trackRow[PlaylistTracksTable.trackId].value,
                    position = trackRow[PlaylistTracksTable.position],
                )
            }

        return Playlist(
            id = playlistId,
            title = this[PlaylistsTable.title],
            description = null,
            owner = owner,
            tracks = tracks,
            isPublic = this[PlaylistsTable.isPublic],
            coverUrl = this[PlaylistsTable.coverUrl],
            createdAt = this[PlaylistsTable.createdAt],
            updatedAt = this[PlaylistsTable.updatedAt],
        )
    }
}
