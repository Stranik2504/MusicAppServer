package dev.stranik.domain.mapper

import dev.stranik.data.dto.PlaylistDto
import dev.stranik.data.dto.PlaylistOwnerDto
import dev.stranik.data.dto.PlaylistTrackDto
import dev.stranik.domain.model.Playlist
import dev.stranik.domain.model.PlaylistOwner
import dev.stranik.domain.model.PlaylistTrack

fun PlaylistOwner.toDto() = PlaylistOwnerDto(
    id = id,
    username = username,
)

fun PlaylistTrack.toDto() = PlaylistTrackDto(
    id = id,
    trackId = trackId,
    position = position,
)

fun Playlist.toDto() = PlaylistDto(
    id = id,
    title = title,
    description = description,
    owner = owner.toDto(),
    tracks = tracks.map { it.toDto() },
    isPublic = isPublic,
    coverUrl = coverUrl,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

