package dev.stranik.controller

import dev.stranik.domain.usecases.GetAllGenresUseCase
import io.ktor.server.application.Application
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

class GenresController(
    private val getAllGenresUseCase: GetAllGenresUseCase
) {
    fun configure(application: Application) {
        application.routing {
            get("/") {
                val genres = getAllGenresUseCase()
                call.respond(genres)
            }
        }
    }
}