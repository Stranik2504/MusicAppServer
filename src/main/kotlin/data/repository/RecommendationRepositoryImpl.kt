package dev.stranik.data.repository

import dev.stranik.domain.model.Album
import dev.stranik.domain.model.HomeRecommendations
import dev.stranik.domain.model.Track
import dev.stranik.domain.model.TrackRecommendations
import dev.stranik.domain.repository.AlbumRepository
import dev.stranik.domain.repository.ListeningHistoryRepository
import dev.stranik.domain.repository.RecommendationRepository
import dev.stranik.domain.repository.TrackRepository

class RecommendationRepositoryImpl(
    private val albumRepository: AlbumRepository,
    private val trackRepository: TrackRepository,
    private val listeningHistoryRepository: ListeningHistoryRepository,
) : RecommendationRepository {
    override suspend fun getHomeRecommendations(userId: Long, limit: Int): HomeRecommendations {
        val safeLimit = limit.coerceAtLeast(1)
        val history = listeningHistoryRepository.getHistoryByUserId(userId)
            .sortedByDescending { it.playedAt }

        val listenedTracks = history.mapNotNull { trackRepository.findById(it.trackId) }
        val listenedTrackIds = listenedTracks.map { it.id }.toSet()
        val seedArtistIds = listenedTracks.map { it.artist.id }.distinct()

        val recommendedTracks = buildTrackRecommendations(seedArtistIds, listenedTrackIds, safeLimit)
        val recommendedAlbums = buildAlbumRecommendations(seedArtistIds, safeLimit)

        return HomeRecommendations(
            albums = recommendedAlbums,
            tracks = recommendedTracks,
        )
    }

    override suspend fun getTrackRecommendations(trackId: Long, limit: Int): TrackRecommendations? {
        val safeLimit = limit.coerceAtLeast(1)
        val baseTrack = trackRepository.findById(trackId) ?: return null
        val albumId = baseTrack.album?.id

        val candidates = buildList {
            addAll(trackRepository.searchTracks(artistId = baseTrack.artist.id, albumId = albumId, limit = safeLimit * 2))
            if (albumId != null) {
                addAll(trackRepository.searchTracks(albumId = albumId, limit = safeLimit * 2))
            }
            addAll(trackRepository.searchTracks(artistId = baseTrack.artist.id, limit = safeLimit * 2))

            val durationMin = (baseTrack.durationSec - 30).coerceAtLeast(1)
            val durationMax = baseTrack.durationSec + 30
            addAll(trackRepository.searchTracks(durationMin = durationMin, durationMax = durationMax, limit = safeLimit * 2))

            addAll(trackRepository.searchTracks(limit = safeLimit * 2))
        }

        val tracks = candidates
            .distinctBy { it.id }
            .filter { it.id != baseTrack.id }
            .sortedWith(compareByDescending<Track> { it.playCount }.thenBy { it.id })
            .take(safeLimit)

        return TrackRecommendations(tracks)
    }

    override suspend fun getArtistRecommendations(artistId: Long, limit: Int): TrackRecommendations? {
        val safeLimit = limit.coerceAtLeast(1)
        val artist = albumRepository.searchAlbums(artistId = artistId, limit = 1).firstOrNull()?.artist
            ?: trackRepository.searchTracks(artistId = artistId, limit = 1).firstOrNull()?.artist
            ?: return null

        val tracks = trackRepository.searchTracks(artistId = artist.id, limit = safeLimit * 2)
            .distinctBy { it.id }
            .sortedWith(compareByDescending<Track> { it.playCount }.thenBy { it.id })
            .take(safeLimit)

        return TrackRecommendations(tracks)
    }

    private suspend fun buildTrackRecommendations(
        seedArtistIds: List<Long>,
        listenedTrackIds: Set<Long>,
        limit: Int,
    ): List<Track> {
        val candidates = buildList {
            seedArtistIds.forEach { artistId ->
                addAll(trackRepository.searchTracks(artistId = artistId, limit = limit * 2))
            }

            if (isEmpty()) {
                addAll(trackRepository.searchTracks(limit = limit * 2))
            }
        }

        return candidates
            .distinctBy { it.id }
            .filterNot { it.id in listenedTrackIds }
            .sortedWith(compareByDescending<Track> { it.playCount }.thenBy { it.id })
            .take(limit)
    }

    private suspend fun buildAlbumRecommendations(
        seedArtistIds: List<Long>,
        limit: Int,
    ): List<Album> {
        val candidates = buildList {
            seedArtistIds.forEach { artistId ->
                addAll(albumRepository.searchAlbums(artistId = artistId, limit = limit * 2))
            }

            if (isEmpty()) {
                addAll(albumRepository.searchAlbums(limit = limit * 2))
            }
        }

        return candidates
            .distinctBy { it.id }
            .sortedWith(compareByDescending<Album> { it.releaseDate }.thenByDescending { it.id })
            .take(limit)
    }
}

