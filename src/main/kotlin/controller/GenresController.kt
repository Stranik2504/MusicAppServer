package dev.stranik.controller

import dev.stranik.domain.usecases.GetAllGenresUseCase
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

class GenresController(
    private val getAllGenresUseCase: GetAllGenresUseCase
) {
    fun configure(route: Route) {
        route.apply {
            get("/") {
                val genres = getAllGenresUseCase()
                call.respond(genres)
            }
        }
    }
}