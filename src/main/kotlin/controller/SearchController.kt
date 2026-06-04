package dev.stranik.controller

import dev.stranik.domain.usecases.SearchAlbumsUseCase
import dev.stranik.domain.usecases.SearchArtistsUseCase
import dev.stranik.domain.usecases.SearchPlaylistsUseCase
import dev.stranik.domain.usecases.SearchTracksUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

class SearchController(
    private val searchTracksUseCase: SearchTracksUseCase,
    private val searchAlbumsUseCase: SearchAlbumsUseCase,
    private val searchArtistsUseCase: SearchArtistsUseCase,
    private val searchPlaylistsUseCase: SearchPlaylistsUseCase,
) {
    fun configure(application: Application) {
        application.routing {
            get("/") {
                val q = call.request.queryParameters["q"]
                val type = call.request.queryParameters["type"] ?: "all"
                val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 20
                val cursor = call.request.queryParameters["cursor"]

                try {
                    val result = mutableMapOf<String, Any?>()

                    if (type == "all" || type == "track") {
                        val artistId = call.request.queryParameters["artistId"]?.toLongOrNull()
                        val albumId = call.request.queryParameters["albumId"]?.toLongOrNull()
                        val durationMin = call.request.queryParameters["durationMin"]?.toIntOrNull()
                        val durationMax = call.request.queryParameters["durationMax"]?.toIntOrNull()

                        val tracks = searchTracksUseCase(
                            q = q,
                            artistId = artistId,
                            albumId = albumId,
                            durationMin = durationMin,
                            durationMax = durationMax,
                            limit = limit,
                            cursor = cursor,
                        )

                        if (type == "track") {
                            call.respond(tracks)
                            return@get
                        }

                        result["tracks"] = tracks
                    }

                    if (type == "all" || type == "album") {
                        val artistId = call.request.queryParameters["artistId"]?.toLongOrNull()
                        val year = call.request.queryParameters["year"]?.toIntOrNull()

                        val albums = searchAlbumsUseCase(
                            artistId = artistId,
                            q = q,
                            year = year,
                            limit = limit,
                            cursor = cursor,
                        )

                        if (type == "album") {
                            call.respond(albums)
                            return@get
                        }

                        result["albums"] = albums
                    }

                    if (type == "all" || type == "artist") {
                        val genre = call.request.queryParameters["genre"]
                        val sort = call.request.queryParameters["sort"]

                        val artists = searchArtistsUseCase(
                            q = q,
                            genre = genre,
                            sort = sort,
                            limit = limit,
                            cursor = cursor,
                        )

                        if (type == "artist") {
                            call.respond(artists)
                            return@get
                        }

                        result["artists"] = artists
                    }

                    if (type == "all" || type == "playlist") {
                        val userId = call.request.queryParameters["userId"]?.toLongOrNull()
                        val publicOnly = call.request.queryParameters["publicOnly"]?.toBoolean()

                        val playlists = searchPlaylistsUseCase(
                            userId = userId,
                            q = q,
                            publicOnly = publicOnly,
                            limit = limit,
                            cursor = cursor,
                        )

                        if (type == "playlist") {
                            call.respond(playlists)
                            return@get
                        }

                        result["playlists"] = playlists
                    }

                    call.respond(result)
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