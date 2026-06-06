package dev.stranik.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class SearchResultDto(
    val tracks: List<TrackDto>?,
    val albums: List<AlbumDto>?,
    val artists: List<ArtistDto>?,
    val playlists: List<PlaylistDto>?,
)
