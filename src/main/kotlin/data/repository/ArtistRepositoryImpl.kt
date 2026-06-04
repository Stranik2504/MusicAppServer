package dev.stranik.data.repository

import dev.stranik.data.databases.AlbumsTable
import dev.stranik.data.databases.ArtistsTable
import dev.stranik.data.databases.FollowedArtistsTable
import dev.stranik.data.databases.GenresTable
import dev.stranik.data.databases.TracksTable
import dev.stranik.domain.model.AlbumArtist
import dev.stranik.domain.model.Artist
import dev.stranik.domain.model.ArtistAlbum
import dev.stranik.domain.model.ArtistDetails
import dev.stranik.domain.model.ArtistTrack
import dev.stranik.domain.model.Genre
import dev.stranik.domain.repository.ArtistRepository
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class ArtistRepositoryImpl : ArtistRepository {
    override suspend fun searchArtists(
        q: String?,
        genre: String?,
        sort: String?,
        limit: Int,
        cursor: String?,
    ): List<Artist> = transaction {
        if (limit <= 0) return@transaction emptyList()

        val artists = ArtistsTable.selectAll().map { it.toArtist() }

        val filteredByQuery = if (q.isNullOrBlank()) {
            artists
        } else {
            val query = q.trim()
            artists.filter {
                it.name.contains(query, ignoreCase = true) ||
                    it.bio.contains(query, ignoreCase = true) ||
                    it.country.contains(query, ignoreCase = true)
            }
        }

        val filteredByGenre = if (genre.isNullOrBlank()) {
            filteredByQuery
        } else {
            val genreQuery = genre.trim()
            val matchingArtistIds = AlbumsTable
                .join(GenresTable, JoinType.INNER, AlbumsTable.genreId, GenresTable.id)
                .selectAll()
                .where {
                    (GenresTable.name like "%$genreQuery%") or (GenresTable.slug like "%$genreQuery%")
                }
                .map { it[AlbumsTable.artistId].value }
                .toSet()

            filteredByQuery.filter { it.id in matchingArtistIds }
        }

        val sorted = when (sort?.lowercase()) {
            "name" -> filteredByGenre.sortedWith(compareBy<Artist> { it.name.lowercase() }.thenBy { it.id })
            "latest" -> filteredByGenre.sortedWith(compareByDescending<Artist> { it.createdAt }.thenBy { it.id })
            else -> filteredByGenre.sortedWith(compareByDescending<Artist> { it.monthlyListeners }.thenBy { it.id })
        }

        val paged = if (cursor.isNullOrBlank()) {
            sorted
        } else {
            val cursorId = cursor.toLongOrNull()
            if (cursorId == null) {
                sorted
            } else {
                val cursorIndex = sorted.indexOfFirst { it.id == cursorId }
                if (cursorIndex == -1) sorted else sorted.drop(cursorIndex + 1)
            }
        }

        paged.take(limit)
    }

    override suspend fun findById(id: Long): Artist? = transaction {
        ArtistsTable
            .selectAll()
            .where { ArtistsTable.id eq id }
            .singleOrNull()
            ?.toArtist()
    }

    override suspend fun getDetails(id: Long): ArtistDetails? = transaction {
        val artist = findArtistRow(id) ?: return@transaction null

        val genres = AlbumsTable
            .join(GenresTable, JoinType.INNER, AlbumsTable.genreId, GenresTable.id)
            .selectAll()
            .where { AlbumsTable.artistId eq id }
            .map {
                Genre(
                    id = it[GenresTable.id].value,
                    name = it[GenresTable.name],
                    slug = it[GenresTable.slug],
                )
            }
            .distinctBy { it.id }

        val topTracks = TracksTable
            .selectAll()
            .where { TracksTable.artistId eq id }
            .orderBy(TracksTable.playCount to SortOrder.DESC, TracksTable.trackNumber to SortOrder.ASC)
            .limit(5)
            .map {
                ArtistTrack(
                    id = it[TracksTable.id].value,
                    title = it[TracksTable.title],
                    durationSec = it[TracksTable.durationSec],
                )
            }

        val albums = AlbumsTable
            .selectAll()
            .where { AlbumsTable.artistId eq id }
            .orderBy(AlbumsTable.releaseDate to SortOrder.DESC, AlbumsTable.id to SortOrder.DESC)
            .map {
                ArtistAlbum(
                    id = it[AlbumsTable.id].value,
                    title = it[AlbumsTable.title],
                )
            }

        val followersCount = FollowedArtistsTable
            .selectAll()
            .where { FollowedArtistsTable.artistId eq id }
            .count()
            .toInt()

        ArtistDetails(
            id = artist.id,
            name = artist.name,
            bio = artist.bio,
            avatarUrl = artist.avatarUrl,
            country = artist.country,
            monthlyListeners = artist.monthlyListeners,
            genres = genres,
            topTracks = topTracks,
            albums = albums,
            followersCount = followersCount,
            createdAt = artist.createdAt,
        )
    }

    private fun findArtistRow(id: Long): Artist? {
        return ArtistsTable
            .selectAll()
            .where { ArtistsTable.id eq id }
            .singleOrNull()
            ?.toArtist()
    }

    private fun org.jetbrains.exposed.sql.ResultRow.toArtist(): Artist = Artist(
        id = this[ArtistsTable.id].value,
        name = this[ArtistsTable.name],
        bio = this[ArtistsTable.bio],
        avatarUrl = this[ArtistsTable.avatarUrl],
        country = this[ArtistsTable.country],
        monthlyListeners = this[ArtistsTable.monthlyListeners],
        createdAt = this[ArtistsTable.createdAt],
    )
}

