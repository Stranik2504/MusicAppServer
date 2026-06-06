package dev.stranik.controller

import dev.stranik.data.dto.AlbumDto
import dev.stranik.data.dto.ArtistDto
import dev.stranik.data.dto.PlaylistDto
import dev.stranik.data.dto.SearchResultDto
import dev.stranik.data.dto.TrackDto
import dev.stranik.domain.usecases.SearchAlbumsUseCase
import dev.stranik.domain.usecases.SearchArtistsUseCase
import dev.stranik.domain.usecases.SearchPlaylistsUseCase
import dev.stranik.domain.usecases.SearchTracksUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

class SearchController(
    private val searchTracksUseCase: SearchTracksUseCase,
    private val searchAlbumsUseCase: SearchAlbumsUseCase,
    private val searchArtistsUseCase: SearchArtistsUseCase,
    private val searchPlaylistsUseCase: SearchPlaylistsUseCase,
) {
    fun configure(route: Route) {
        route.apply {
            get("") {
                val q = call.request.queryParameters["q"]
                val type = call.request.queryParameters["type"] ?: "all"
                val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 20
                val cursor = call.request.queryParameters["cursor"]

                try {
                    var tracks = emptyList<TrackDto>()
                    var albums = emptyList<AlbumDto>()
                    var artists = emptyList<ArtistDto>()
                    var playlists = emptyList<PlaylistDto>()

                    if (type == "all" || type == "track") {
                        val artistId = call.request.queryParameters["artistId"]?.toLongOrNull()
                        val albumId = call.request.queryParameters["albumId"]?.toLongOrNull()
                        val durationMin = call.request.queryParameters["durationMin"]?.toIntOrNull()
                        val durationMax = call.request.queryParameters["durationMax"]?.toIntOrNull()

                        val tracksSearch = searchTracksUseCase(
                            q = q,
                            artistId = artistId,
                            albumId = albumId,
                            durationMin = durationMin,
                            durationMax = durationMax,
                            limit = limit,
                            cursor = cursor,
                        )

                        if (type == "track") {
                            call.respond(tracksSearch)
                            return@get
                        }

                        tracks = tracksSearch
                    }

                    if (type == "all" || type == "album") {
                        val artistId = call.request.queryParameters["artistId"]?.toLongOrNull()
                        val year = call.request.queryParameters["year"]?.toIntOrNull()

                        val albumsSearch = searchAlbumsUseCase(
                            artistId = artistId,
                            q = q,
                            year = year,
                            limit = limit,
                            cursor = cursor,
                        )

                        if (type == "album") {
                            call.respond(albumsSearch)
                            return@get
                        }

                        albums = albumsSearch
                    }

                    if (type == "all" || type == "artist") {
                        val genre = call.request.queryParameters["genre"]
                        val sort = call.request.queryParameters["sort"]

                        val artistsSearch = searchArtistsUseCase(
                            q = q,
                            genre = genre,
                            sort = sort,
                            limit = limit,
                            cursor = cursor,
                        )

                        if (type == "artist") {
                            call.respond(artistsSearch)
                            return@get
                        }

                        artists = artistsSearch
                    }

                    if (type == "all" || type == "playlist") {
                        val userId = call.request.queryParameters["userId"]?.toLongOrNull()
                        val publicOnly = call.request.queryParameters["publicOnly"]?.toBoolean()

                        val playlistsSearch = searchPlaylistsUseCase(
                            userId = userId,
                            q = q,
                            publicOnly = publicOnly,
                            limit = limit,
                            cursor = cursor,
                        )

                        if (type == "playlist") {
                            call.respond(playlistsSearch)
                            return@get
                        }

                        playlists = playlistsSearch
                    }

                    call.respond(
                        SearchResultDto(
                            tracks = tracks,
                            albums = albums,
                            artists = artists,
                            playlists = playlists,
                        )
                    )
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to (e.message ?: "Ошибка")))
                }
            }

            get("/suggestions") {
                val q = call.request.queryParameters["q"]

                if (q.isNullOrBlank()) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "q не задан"))
                    return@get
                }

                val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 5

                val tracks = searchTracksUseCase(q = q, limit = limit)
                val albums = searchAlbumsUseCase(q = q, limit = limit)
                val artists = searchArtistsUseCase(q = q, limit = limit)

                call.respond(mapOf("tracks" to tracks.map { it.title }, "albums" to albums.map { it.title }, "artists" to artists.map { it.name }))
            }
        }
    }
}