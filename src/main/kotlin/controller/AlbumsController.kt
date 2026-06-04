package dev.stranik.controller

import dev.stranik.domain.usecases.GetAlbumUseCase
import dev.stranik.domain.usecases.SearchAlbumsUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

class AlbumsController(
    private val searchAlbumsUseCase: SearchAlbumsUseCase,
    private val getAlbumUseCase: GetAlbumUseCase,
) {
    fun configure(application: Application) {
        application.routing {
            get("/") {
                val artistId = call.request.queryParameters["artistId"]?.toLongOrNull()
                val q = call.request.queryParameters["q"]
                val year = call.request.queryParameters["year"]?.toIntOrNull()
                val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 20
                val cursor = call.request.queryParameters["cursor"]

                val albums = searchAlbumsUseCase(
                    artistId = artistId,
                    q = q,
                    year = year,
                    limit = limit,
                    cursor = cursor,
                )

                call.respond(albums)
            }

            get("/{albumId}") {
                val albumIdParam = call.parameters["albumId"]

                if (albumIdParam == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "albumId не задан"))
                    return@get
                }

                val albumId = albumIdParam.toLongOrNull()

                if (albumId == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Неверный albumId"))
                    return@get
                }

                val album = getAlbumUseCase(albumId)

                if (album == null) {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Альбом не найден"))
                    return@get
                }

                call.respond(album)
            }
        }
    }
}