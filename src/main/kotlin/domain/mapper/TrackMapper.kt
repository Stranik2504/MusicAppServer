package dev.stranik.domain.mapper

import dev.stranik.data.dto.TrackDto
import dev.stranik.domain.model.Track

fun Track.toDto() = TrackDto(
    id = id,
    title = title,
    artist = artist.toDto(),
    album = album?.toDto(),
    durationSec = durationSec,
    playCount = playCount,
    createdAt = createdAt,
)

