package dev.stranik.domain.repository

import dev.stranik.domain.model.Album

interface AlbumRepository {
    suspend fun searchAlbums(
        artistId: Long? = null,
        q: String? = null,
        year: Int? = null,
        limit: Int = 20,
        cursor: String? = null,
    ): List<Album>

    suspend fun findById(id: Long): Album?
}

