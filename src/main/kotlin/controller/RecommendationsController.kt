package dev.stranik.controller

import dev.stranik.domain.usecases.GetArtistRecommendationsUseCase
import dev.stranik.domain.usecases.GetHomeRecommendationsUseCase
import dev.stranik.domain.usecases.GetTrackRecommendationsUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

class RecommendationsController(
    private val getHomeRecommendationsUseCase: GetHomeRecommendationsUseCase,
    private val getTrackRecommendationsUseCase: GetTrackRecommendationsUseCase,
    private val getArtistRecommendationsUseCase: GetArtistRecommendationsUseCase,
) {
    fun configure(route: Route) {
        route.apply {
            get("/home") {
                val jwtUserId = call.principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asLong()

                if (jwtUserId == null) {
                    call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Пользователь не авторизован"))
                    return@get
                }

                val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10
                val recommendations = getHomeRecommendationsUseCase(jwtUserId, limit)

                call.respond(recommendations)
            }

            get("/for-track/{trackId}") {
                val trackIdParam = call.parameters["trackId"]

                if (trackIdParam == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "trackId не задан"))
                    return@get
                }

                val trackId = trackIdParam.toLongOrNull()

                if (trackId == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Неверный trackId"))
                    return@get
                }

                val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10
                val recommendations = getTrackRecommendationsUseCase(trackId, limit)

                if (recommendations == null) {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Трек не найден"))
                    return@get
                }

                call.respond(recommendations)
            }

            get("/for-artist/{artistId}") {
                val artistIdParam = call.parameters["artistId"]

                if (artistIdParam == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "artistId не задан"))
                    return@get
                }

                val artistId = artistIdParam.toLongOrNull()

                if (artistId == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Неверный artistId"))
                    return@get
                }

                val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10
                val recommendations = getArtistRecommendationsUseCase(artistId, limit)

                if (recommendations == null) {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Артист не найден"))
                    return@get
                }

                call.respond(recommendations)
            }
        }
    }
}