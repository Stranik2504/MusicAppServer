package dev.stranik.data.repository

import dev.stranik.data.databases.AlbumsTable
import dev.stranik.data.databases.ArtistsTable
import dev.stranik.data.databases.GenresTable
import dev.stranik.data.databases.TracksTable
import dev.stranik.domain.model.Album
import dev.stranik.domain.model.AlbumArtist
import dev.stranik.domain.model.AlbumTrack
import dev.stranik.domain.model.Genre
import dev.stranik.domain.repository.AlbumRepository
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.javatime.year
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class AlbumRepositoryImpl : AlbumRepository {
    override suspend fun searchAlbums(
        artistId: Long?,
        q: String?,
        year: Int?,
        limit: Int,
        cursor: String?,
    ): List<Album> = transaction {
        val query = AlbumsTable
            .join(ArtistsTable, JoinType.INNER, AlbumsTable.artistId, ArtistsTable.id)
            .join(GenresTable, JoinType.INNER, AlbumsTable.genreId, GenresTable.id)
            .selectAll()
            .apply {
                if (artistId != null) {
                    andWhere { AlbumsTable.artistId eq artistId }
                }

                if (!q.isNullOrBlank()) {
                    andWhere { AlbumsTable.title like "%$q%" }
                }

                if (year != null) {
                    andWhere { AlbumsTable.releaseDate.year() eq year }
                }

                if (!cursor.isNullOrBlank()) {
                    val cursorId = cursor.toLongOrNull()
                    if (cursorId != null) {
                        andWhere { AlbumsTable.id greater cursorId }
                    }
                }

                orderBy(AlbumsTable.id to SortOrder.ASC)
                limit(limit)
            }

        query.map { row -> row.toAlbum() }
    }

    override suspend fun findById(id: Long): Album? = transaction {
        val row = AlbumsTable
            .join(ArtistsTable, JoinType.INNER, AlbumsTable.artistId, ArtistsTable.id)
            .join(GenresTable, JoinType.INNER, AlbumsTable.genreId, GenresTable.id)
            .selectAll()
            .where { AlbumsTable.id eq id }
            .singleOrNull() ?: return@transaction null

        row.toAlbum()
    }

    private fun ResultRow.toAlbum(): Album {
        val artist = AlbumArtist(
            id = this[ArtistsTable.id].value,
            name = this[ArtistsTable.name],
        )

        val genre = Genre(
            id = this[GenresTable.id].value,
            name = this[GenresTable.name],
            slug = this[GenresTable.slug],
        )

        val tracks = TracksTable
            .selectAll()
            .where { TracksTable.albumId eq this@toAlbum[AlbumsTable.id].value }
            .orderBy(TracksTable.trackNumber to SortOrder.ASC)
            .map { trackRow ->
                AlbumTrack(
                    id = trackRow[TracksTable.id].value,
                    title = trackRow[TracksTable.title],
                    durationSec = trackRow[TracksTable.durationSec],
                    trackNumber = trackRow[TracksTable.trackNumber],
                )
            }

        return Album(
            id = this[AlbumsTable.id].value,
            title = this[AlbumsTable.title],
            artist = artist,
            releaseDate = this[AlbumsTable.releaseDate],
            tracks = tracks,
            coverUrl = this[AlbumsTable.coverUrl],
            genre = genre,
            albumType = this[AlbumsTable.albumType],
            createdAt = this[AlbumsTable.createdAt],
        )
    }
}

