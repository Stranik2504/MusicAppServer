package dev.stranik.controller

import io.ktor.server.application.Application
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

class GenresController {
    fun configure(application: Application) {
        application.routing {
            get("/") {

            }
        }
    }
}