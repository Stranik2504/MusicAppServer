package dev.stranik.data.repository

import dev.stranik.data.databases.AlbumsTable
import dev.stranik.data.databases.ArtistsTable
import dev.stranik.data.databases.LikedTracksTable
import dev.stranik.data.databases.TracksTable
import dev.stranik.domain.model.AlbumArtist
import dev.stranik.domain.model.Track
import dev.stranik.domain.repository.TrackRepository
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class TrackRepositoryImpl : TrackRepository {
    override suspend fun searchTracks(
        q: String?,
        artistId: Long?,
        albumId: Long?,
        durationMin: Int?,
        durationMax: Int?,
        limit: Int,
        cursor: String?,
    ): List<Track> = transaction {
        val query = TracksTable
            .join(ArtistsTable, JoinType.LEFT, TracksTable.artistId, ArtistsTable.id)
            .selectAll()
            .apply {
                if (!q.isNullOrBlank()) {
                    andWhere { TracksTable.title like "%$q%" }
                }

                if (artistId != null) {
                    andWhere { TracksTable.artistId eq artistId }
                }

                if (albumId != null) {
                    andWhere { TracksTable.albumId eq albumId }
                }

                if (durationMin != null) {
                    andWhere { TracksTable.durationSec greaterEq durationMin }
                }

                if (durationMax != null) {
                    andWhere { TracksTable.durationSec lessEq durationMax }
                }

                if (!cursor.isNullOrBlank()) {
                    val cursorId = cursor.toLongOrNull()
                    if (cursorId != null) {
                        andWhere { TracksTable.id greater cursorId }
                    }
                }

                orderBy(TracksTable.id to SortOrder.ASC)
                limit(limit)
            }

        query.map { row -> row.toTrack() }
    }

    override suspend fun findById(id: Long): Track? = transaction {
        val row = TracksTable
            .join(ArtistsTable, JoinType.LEFT, TracksTable.artistId, ArtistsTable.id)
            .join(AlbumsTable, JoinType.LEFT, TracksTable.albumId, AlbumsTable.id)
            .selectAll()
            .where { TracksTable.id eq id }
            .singleOrNull() ?: return@transaction null

        row.toTrack()
    }

    override suspend fun getStreamUrl(id: Long): String? = transaction {
        TracksTable
            .selectAll().where { TracksTable.id eq id }
            .singleOrNull()
            ?.get(TracksTable.fileUrl)
    }

    override suspend fun likeTrack(userId: Long, trackId: Long): Boolean = transaction {
        LikedTracksTable.insertIgnore {
            it[LikedTracksTable.userId] = userId
            it[LikedTracksTable.trackId] = trackId
        }

        true
    }

    override suspend fun unlikeTrack(userId: Long, trackId: Long): Boolean = transaction {
        val deleted = LikedTracksTable.deleteWhere { LikedTracksTable.userId eq userId and (LikedTracksTable.trackId eq trackId) }
        deleted > 0
    }

    override suspend fun getLikesCount(trackId: Long): Int = transaction {
        LikedTracksTable.selectAll().andWhere { LikedTracksTable.trackId eq trackId }.count().toInt()
    }

    private fun ResultRow.toTrack(): Track {
        val artist = AlbumArtist(
            id = this[ArtistsTable.id].value,
            name = this[ArtistsTable.name],
        )

        var album: AlbumArtist? = null

        if (this[TracksTable.albumId] != null) {
            album = AlbumArtist(
                id = this[AlbumsTable.id].value,
                name = this[AlbumsTable.title],
            )
        }

        return Track(
            id = this[TracksTable.id].value,
            title = this[TracksTable.title],
            artist = artist,
            album = album,
            durationSec = this[TracksTable.durationSec],
            playCount = this[TracksTable.playCount],
            fileUrl = this[TracksTable.fileUrl],
            createdAt = this[TracksTable.createdAt],
        )
    }
}



