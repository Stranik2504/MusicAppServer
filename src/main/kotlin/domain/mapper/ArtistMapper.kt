package dev.stranik.domain.mapper

import dev.stranik.data.dto.AlbumDto
import dev.stranik.data.dto.ArtistAlbumDto
import dev.stranik.data.dto.ArtistDetailsDto
import dev.stranik.data.dto.ArtistDto
import dev.stranik.data.dto.ArtistTrackDto
import dev.stranik.data.dto.GenreDto
import dev.stranik.domain.model.Artist
import dev.stranik.domain.model.ArtistAlbum
import dev.stranik.domain.model.ArtistDetails
import dev.stranik.domain.model.ArtistTrack

fun Artist.toDto() = ArtistDto(
    name = name,
    bio = bio,
    avatarUrl = avatarUrl,
    country = country,
    monthlyListeners = monthlyListeners,
    createdAt = createdAt,
)

fun ArtistTrack.toDto() = ArtistTrackDto(
    id = id,
    title = title,
    durationSec = durationSec,
)

fun ArtistAlbum.toDto() = ArtistAlbumDto(
    id = id,
    title = title,
)

fun ArtistDetails.toDto() = ArtistDetailsDto(
    id = id,
    name = name,
    bio = bio,
    avatarUrl = avatarUrl,
    country = country,
    monthlyListeners = monthlyListeners,
    genres = genres.map { GenreDto(name = it.name, slug = it.slug) },
    topTracks = topTracks.map { it.toDto() },
    albums = albums.map { it.toDto() },
    followersCount = followersCount,
    createdAt = createdAt,
)

