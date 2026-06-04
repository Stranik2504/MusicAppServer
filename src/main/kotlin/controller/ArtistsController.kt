package dev.stranik.controller

import dev.stranik.domain.usecases.FollowArtistUseCase
import dev.stranik.domain.usecases.GetArtistUseCase
import dev.stranik.domain.usecases.SearchArtistsUseCase
import dev.stranik.domain.usecases.UnfollowArtistUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

class ArtistsController(
    private val searchArtistsUseCase: SearchArtistsUseCase,
    private val getArtistUseCase: GetArtistUseCase,
    private val followArtistUseCase: FollowArtistUseCase,
    private val unfollowArtistUseCase: UnfollowArtistUseCase,
) {
    fun configure(application: Application) {
        application.routing {
            post("/{artistId}/follow") {
                val artistIdParam = call.parameters["artistId"]
                val artistId = artistIdParam?.toLongOrNull()

                if (artistId == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Неверный artistId"))
                    return@post
                }

                val jwtUserId = call.principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asLong()

                if (jwtUserId == null) {
                    call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Пользователь не авторизован"))
                    return@post
                }

                val result = followArtistUseCase(jwtUserId, artistId)

                if (!result) {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Артист не найден"))
                    return@post
                }

                call.respond(HttpStatusCode.OK, mapOf("message" to "Вы успешно подписались на артиста"))
            }

            delete("/{artistId}/follow") {
                val artistIdParam = call.parameters["artistId"]
                val artistId = artistIdParam?.toLongOrNull()

                if (artistId == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Неверный artistId"))
                    return@delete
                }

                val jwtUserId = call.principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asLong()

                if (jwtUserId == null) {
                    call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Пользователь не авторизован"))
                    return@delete
                }

                val result = unfollowArtistUseCase(jwtUserId, artistId)

                if (!result) {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Подписка не найдена"))
                    return@delete
                }

                call.respond(HttpStatusCode.OK, mapOf("message" to "Вы успешно отписались от артиста"))
            }

            get("/") {
                val q = call.request.queryParameters["q"]
                val genre = call.request.queryParameters["genre"]
                val sort = call.request.queryParameters["sort"] ?: "popularity"
                val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 20
                val cursor = call.request.queryParameters["cursor"]

                val artists = searchArtistsUseCase(
                    q = q,
                    genre = genre,
                    sort = sort,
                    limit = limit,
                    cursor = cursor,
                )

                call.respond(artists)
            }

            get("/{artistId}") {
                val artistIdParam = call.parameters["artistId"]
                val artistId = artistIdParam?.toLongOrNull()

                if (artistId == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Неверный artistId"))
                    return@get
                }

                val artist = getArtistUseCase(artistId)

                if (artist == null) {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Артист не найден"))
                    return@get
                }

                call.respond(artist)
            }
        }
    }
}