package dev.stranik.domain.repository

import dev.stranik.domain.model.Artist
import dev.stranik.domain.model.ArtistDetails

interface ArtistRepository {
    suspend fun searchArtists(
        q: String? = null,
        genre: String? = null,
        sort: String? = "popularity",
        limit: Int = 20,
        cursor: String? = null,
    ): List<Artist>

    suspend fun findById(id: Long): Artist?

    suspend fun getDetails(id: Long): ArtistDetails?
}

