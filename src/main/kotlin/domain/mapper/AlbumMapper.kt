package dev.stranik.domain.mapper

import dev.stranik.data.databases.ArtistsTable
import dev.stranik.data.databases.GenresTable
import dev.stranik.data.databases.TracksTable
import dev.stranik.data.dto.AlbumArtistDto
import dev.stranik.data.dto.AlbumDto
import dev.stranik.data.dto.AlbumTrackDto
import dev.stranik.data.dto.GenreDto
import dev.stranik.domain.model.Album
import dev.stranik.domain.model.AlbumArtist
import dev.stranik.domain.model.AlbumTrack
import dev.stranik.domain.model.Genre

fun AlbumArtist.toDto() = AlbumArtistDto(
    id = id,
    name = name,
)

fun Genre.toDto() = GenreDto(
    name = name,
    slug = slug,
)

fun AlbumTrack.toDto() = AlbumTrackDto(
    id = id,
    title = title,
    durationSec = durationSec,
    trackNumber = trackNumber,
)

fun Album.toDto() = AlbumDto(
    id = id,
    title = title,
    artist = artist.toDto(),
    releaseDate = releaseDate,
    tracks = tracks.map { it.toDto() },
    coverUrl = coverUrl,
    genre = genre.toDto(),
    albumType = albumType,
    createdAt = createdAt,
)
